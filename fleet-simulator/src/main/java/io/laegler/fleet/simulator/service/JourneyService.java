package io.laegler.fleet.simulator.service;

import static io.laegler.fleet.model.Status.CANCELLED;
import static io.laegler.fleet.model.Status.ENROUTE;
import static io.laegler.fleet.model.Status.PICKUP;
import static io.laegler.fleet.model.Status.REQUEST;
import static io.laegler.fleet.model.Status.SCHEDULED;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.google.common.collect.ImmutableMap;
import io.laegler.fleet.model.Journey;
import io.laegler.fleet.model.Position;
import io.laegler.fleet.model.Provider;
import io.laegler.fleet.model.Trip;
import io.laegler.fleet.model.Vehicle;
import io.laegler.fleet.repo.JourneyEsRepository;
import io.laegler.fleet.repo.ProviderEsRepository;
import io.laegler.fleet.repo.TripEsRepository;
import io.laegler.fleet.repo.VehicleEsRepository;
import io.laegler.fleet.simulator.config.FleetSimulatorProperties;
import io.laegler.fleet.simulator.geo.GeoGoogleService;
import io.laegler.fleet.simulator.http.NotFoundException;
import io.laegler.fleet.simulator.http.OutOfRangeException;
import io.laegler.fleet.simulator.schedule.NotifyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JourneyService {

  @Autowired
  private FleetSimulatorProperties props;

  @Autowired
  private NotifyService notifyService;

  @Autowired
  private GeoGoogleService geoService;

  @Autowired
  private JourneyEsRepository journeyRepo;

  @Autowired
  private VehicleEsRepository vehicleRepo;

  @Autowired
  private TripEsRepository tripRepo;

  @Autowired
  private ProviderEsRepository providerRepo;

  @Autowired
  private TripService tripService;

  @Autowired
  private VehicleService vehicleService;

  public Journey planJourney(Journey journey) {
    log.trace("planJourney({}) called", journey);

    validateJourney(journey);

    Optional<Vehicle> vehicle = vehicleService.getClosestAvailableVehicle(journey.getFrom());
    if (!vehicle.isPresent()) {
      journey.setAvailable(false);
      journey.setDescription("There is no Vehicle available right now");
    } else {
      Provider provider =
          ((Page<Provider>) providerRepo.findAll()).getContent().stream().findFirst().get();
      journey.setFare(calculateFare(journey));
      journey.setStatus(REQUEST);
      journey.setProviderId(provider.getProviderId());
      journey.setAvailable(true);
    }

    // journeyRepo.save(journey);

    return journey;
  }

  public Journey bookJourney(Journey journey) {
    log.trace("bookJourney({}) called", journey);

    validateJourney(journey);

    journey.setTripIds(new ArrayList<String>());

    // Get closest available Vehicle
    Vehicle vehicle = vehicleService.bookClosestAvailableVehicle(journey);

    Trip pickupTrip = tripService.bookTrip(Trip.builder().from(vehicle.getPosition())
        .to(journey.getFrom()).position(vehicle.getPosition()).status(PICKUP)
        .vehicleId(vehicle.getVehicleId()).vehicle(vehicle).build());
    journey.getTripIds().add(pickupTrip.getTripId());

    Trip trip = tripService.bookTrip(Trip.builder().vehicleId(vehicle.getVehicleId())
        .vehicle(vehicle).from(journey.getFrom()).to(journey.getTo()).status(SCHEDULED)
        .start(pickupTrip.getEta().plusSeconds(20)).build());
    journey.getTripIds().add(trip.getTripId());

    journey
        .setDescription("Taxi is going to pick up a passenger." + getJourneyDescription(journey));
    journey.setEta(trip.getEta());
    journey.setAvailable(true);
    journey.setActive(true);
    journey.setPassenger(journey.getPassenger());
    journey.setStatus(ENROUTE);
    journey.setFare(calculateFare(journey));
    journey.setCurrency("NZD");
    if (journey.getStart() == null) {
      journey.setStart(LocalDateTime.now(ZoneId.of(props.getTimezone())));
    }
    journey.setBookingId("BOOKING" + System.currentTimeMillis());
    journey.setVehicle(vehicle);
    journey.setPosition(vehicle.getPosition());
    journey.setTrips(ImmutableMap.of(pickupTrip.getTripId(), pickupTrip, trip.getTripId(), trip));
    journey.setProviderId("MOCK-TAXI");
    journey.setTimestampUTC(System.currentTimeMillis());

    notifyService.notify("journey", journey.getBookingId());
    return journeyRepo.save(sanitizeJourney(journey));
  }

  public Journey cancelJourney(final String bookingId) {
    log.trace("cancelJourney({}) called", bookingId);

    Journey journey = journeyRepo.findFirstByBookingId(bookingId).get();
    journey.setStatus(CANCELLED);
    journey.setActive(false);
    journey.setTimestampUTC(System.currentTimeMillis());
    journey.getTripIds().forEach(id -> tripService.cancelTrip(id));

    return journeyRepo.save(journey);
  }

  public Journey trackJourney(final String bookingId) {
    log.trace("trackJourney({}) called", bookingId);

    Journey journey = journeyRepo.findById(bookingId).orElseThrow(
        () -> new NotFoundException("Cannot find Journey by Booking ID: " + bookingId));

    return sanitizeJourney(journey);
  }

  public List<Journey> getAllJourneys() {
    log.trace("getAllJourneys() called");

    List<Journey> journeys = ((Page<Journey>) journeyRepo.findAll()).getContent();

    return journeys;
  }

  public List<Journey> getActiveJourneys() {
    return journeyRepo.findByActiveTrue().stream().map(j -> sanitizeJourney(j)).collect(toList());
  }

  public Journey getJourneyByTripId(String tripId) {
    return journeyRepo.findFirstByTripIds(tripId).map(j -> sanitizeJourney(j))
        .orElseThrow(() -> new NotFoundException("Cannot find Journey by Trip ID " + tripId));
  }

  public List<Journey> saveJourneys(List<Journey> journeys) {
    return ((List<Journey>) journeyRepo.saveAll(journeys)).stream().map(j -> sanitizeJourney(j))
        .collect(toList());
  }

  private void sanitizePosition(Journey journey) {
    Position from = journey.getFrom();
    if (from.getLat() != 0.0 && from.getLng() != 0.0) {
      // journey.getFrom().setAddress(address);
      // journey.getFrom().setPlaceId(geoService.);Address(address);
      journey.getFrom().setLocation(from.getLat() + "," + from.getLng());
    } else if (from.getPlaceId() != null && !from.getPlaceId().isEmpty()) {
      journey.setFrom(geoService.getPositionByPlaceId(from.getPlaceId()).orElse(null));
    } else if (from.getAddress() != null && !from.getAddress().isEmpty()) {
      journey.setFrom(geoService.getPositionByAdress(journey.getFrom().getAddress()).orElse(null));
    }

    Position to = journey.getTo();
    if (to.getLat() != 0.0 && to.getLng() != 0.0) {
      journey.getTo().setLocation(to.getLat() + "," + to.getLng());
    } else if (to.getPlaceId() != null && !to.getPlaceId().isEmpty()) {
      journey.setTo(geoService.getPositionByPlaceId(to.getPlaceId()).orElse(null));
    } else if (to.getAddress() != null && !to.getAddress().isEmpty()) {
      journey.setTo(geoService.getPositionByAdress(journey.getTo().getAddress()).orElse(null));
    }
  }

  private Journey sanitizeJourney(Journey journey) {
    journey.setAvailable(true);
    if (journey.getProvider() == null && journey.getProviderId() != null) {
      journey.setProvider(providerRepo.findById(journey.getProviderId())
          .orElse(Provider.builder().providerId(journey.getProviderId()).build()));
    } else if (journey.getProvider() != null && journey.getProviderId() == null) {
      journey.setProviderId(journey.getProvider().getProviderId());
    }
    if (isEmpty(journey.getTrips()) && !isEmpty(journey.getTripIds())) {
      journey.setTrips(((List<Trip>) tripRepo.findAllById(journey.getTripIds())).stream()
          .collect(toMap(Trip::getTripId, Function.identity())));
    } else {
      journey.setTripIds(journey.getTrips().values().stream().map(t -> t.getTripId())
          .collect(Collectors.toList()));
    }
    if (journey.getVehicle() == null && journey.getVehicleId() != null) {
      journey.setVehicle(vehicleRepo.findById(journey.getVehicleId()).orElse(null));
    } else {
      journey.setVehicleId(journey.getVehicle().getVehicleId());
    }
    return journey;
  }

  private void validateJourney(Journey journey) {
    Assert.notNull(journey, "Journey cannot be null");
    Assert.notNull(journey.getFrom(), "From cannot be null");
    Assert.notNull(journey.getTo(), "To cannot be null");
    Assert.state(journey.getFrom().getLat() != journey.getTo().getLat(),
        "From and To is the same location");

    sanitizePosition(journey);

    if (!isInRange(journey.getFrom(), journey.getTo())) {
      throw new OutOfRangeException("The requested Journey is out of range");
    }
  }

  private BigDecimal calculateFare(Journey journey) {
    return BigDecimal.valueOf(20.0);
    // return BigDecimal.valueOf(ChronoUnit.MINUTES.between(now(), journey.getEta()));
  }

  private String getJourneyDescription(Journey journey) {
    return new StringBuilder(" A journey from ").append(journey.getFrom()).append(" (")
        .append(journey.getFrom().getLocation()).append(") to ").append(journey.getTo())
        .append(" (").append(journey.getTo().getLocation()).append(")").toString();
  }

  private boolean isInRange(Position... pos) {
    List<Position> positions = asList(pos);
    positions.stream().filter(p -> p.getLat() == 0 && p.getLng() == 0 && p.getPlaceId() != null)
        .forEach(p -> p = geoService.getPositionByPlaceId(p.getPlaceId())
            .orElseThrow(() -> new IllegalStateException("Invalid position")));

    Position operationBase = geoService.getPositionByAdress(props.getCity()).orElseThrow(
        () -> new IllegalStateException("Cannot resolve Address to Position: " + props.getCity()));

    // +/- 10 Latitude and +/- 10 Longitude around the Operation area is in range
    List<Position> positionsOutOfRange = positions.stream().filter(p -> {
      return p.getLat() > operationBase.getLat() + 10 || p.getLng() > operationBase.getLng() + 10
          || p.getLat() < operationBase.getLat() - 10 || p.getLng() < operationBase.getLng() - 10;
    }).collect(toList());

    return !positionsOutOfRange.stream().findFirst().isPresent();
  }

}

