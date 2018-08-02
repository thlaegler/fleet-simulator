package io.laegler.fleet.simulator.service;

import static io.laegler.fleet.model.Status.CANCELLED;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Arrays.asList;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.google.maps.model.DirectionsRoute;
import io.laegler.fleet.model.Trip;
import io.laegler.fleet.model.Vehicle;
import io.laegler.fleet.repo.TripEsRepository;
import io.laegler.fleet.repo.VehicleEsRepository;
import io.laegler.fleet.simulator.geo.GeoGoogleService;
import io.laegler.fleet.simulator.schedule.NotifyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TripService {

  @Autowired
  private GeoGoogleService geoService;

  @Autowired
  private NotifyService notifyService;

  @Autowired
  private TripEsRepository tripRepo;

  @Autowired
  private VehicleEsRepository vehicleRepo;

  @Autowired
  private VehicleService vehicleService;

  public Trip bookTrip(final Trip trip) {
    return bookTrip(trip, true);
  }

  public Trip bookTrip(final Trip trip, boolean forceGenerateRoute) {
    log.trace("bookTrip({}) called", trip);

    Assert.notNull(trip, "Trip cannot be null");
    Assert.notNull(trip.getFrom(), "From cannot be null");
    Assert.notNull(trip.getTo(), "To cannot be null");

    Vehicle vehicle = trip.getVehicle();
    if (vehicle == null) {
      if (trip.getVehicleId() != null) {
        vehicle = vehicleRepo.findById(trip.getVehicleId()).orElseThrow(
            () -> new IllegalStateException("Cannot find Vehicle with ID " + trip.getVehicleId()));
      } else {
        vehicle = vehicleService.getClosestAvailableVehicle(trip.getFrom())
            .orElseThrow(() -> new IllegalStateException("Cannot find available Vehicle"));
      }
    }

    if (trip.getRoute() == null) {
      // Calculate actual Driving Route (after Pick-up)
      DirectionsRoute route = geoService.getRouteByTrip(trip)
          .orElseThrow(() -> new IllegalStateException("Cannot calculate Route"));
      trip.setEta(extractEtaFromRoute(route));
      trip.setRoute(route);
    }

    trip.setDescription("Taxi is going to pick up a passenger." + getTripDescription(trip));
    trip.setPassenger(trip.getPassenger());
    // trip.setStatus(ENROUTE);
    trip.setFare(calculateFare(trip));
    trip.setCurrency("NZD");
    trip.setPosition(vehicle.getPosition());
    if (trip.getStart() == null) {
      trip.setStart(LocalDateTime.now());
    }
    trip.setVehicleId(vehicle.getVehicleId());
    trip.setTimestampUTC(System.currentTimeMillis());
    tripRepo.save(trip);
    notifyService.notify("trip", trip.getTripId());

    return trip;
  }

  public void cancelTrip(final String tripId) {
    log.trace("cancelTrip({}) called", tripId);

    Trip trip = tripRepo.findById(tripId).get();
    trip.setStatus(CANCELLED);

    vehicleService.cancelVehicle(trip.getVehicleId());

    tripRepo.save(trip);
    notifyService.notify("trip", trip.getTripId());
  }

  public Trip trackTrip(final String tripId) {
    log.trace("trackTrip({}) called", tripId);

    Trip trip = tripRepo.findById(tripId)
        .orElseThrow(() -> new IllegalStateException("Cannot find Trip by ID: " + tripId));

    return trip;
  }

  public List<Trip> getAllTrips() {
    log.trace("getAllTrips() called");

    List<Trip> trips = ((Page<Trip>) tripRepo.findAll()).getContent();

    return trips;
  }

  private BigDecimal calculateFare(Trip trip) {
    return BigDecimal.valueOf(MINUTES.between(now(), trip.getEta()));
  }

  private String getTripDescription(Trip trip) {
    return new StringBuilder(" A trip from ").append(trip.getFrom()).append(" (")
        .append(trip.getFrom().getLocation()).append(") to ").append(trip.getTo()).append(" (")
        .append(trip.getTo().getLocation()).append(")").toString();
  }

  private LocalDateTime extractEtaFromRoute(DirectionsRoute route) {
    return now().plusSeconds(
        asList(route.legs).stream().reduce((a, b) -> b).orElse(null).durationInTraffic.inSeconds);
  }

}

