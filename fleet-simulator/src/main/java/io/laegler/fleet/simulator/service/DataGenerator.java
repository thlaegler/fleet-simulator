package io.laegler.fleet.simulator.service;

import static io.laegler.fleet.model.Status.AVAILABLE;
import static io.laegler.fleet.model.Status.ENROUTE;
import static io.laegler.fleet.model.VehicleType.TAXI;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.laegler.fleet.gtfs.model.Route;
import io.laegler.fleet.gtfs.model.Stop;
import io.laegler.fleet.gtfs.model.StopTime;
import io.laegler.fleet.gtfs.repo.GtfsStopTimeJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsTripJpaRepository;
import io.laegler.fleet.model.Journey;
import io.laegler.fleet.model.Passenger;
import io.laegler.fleet.model.Position;
import io.laegler.fleet.model.Position.PositionBuilder;
import io.laegler.fleet.model.Provider;
import io.laegler.fleet.model.ProviderType;
import io.laegler.fleet.model.Trip;
import io.laegler.fleet.model.Vehicle;
import io.laegler.fleet.model.VehicleType;
import io.laegler.fleet.repo.JourneyEsRepository;
import io.laegler.fleet.repo.ProviderEsRepository;
import io.laegler.fleet.repo.TripEsRepository;
import io.laegler.fleet.repo.VehicleEsRepository;
import io.laegler.fleet.simulator.config.FleetSimulatorProperties;
import io.laegler.fleet.simulator.geo.GeoGoogleService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataGenerator {

  @Value("${fleet-simulator.id:EXAMPLE-PROVIDER}")
  private String providerId;

  @Value("${fleet-simulator.name:Example Mobility Company}")
  private String providerName;

  @Value("${fleet-simulator.type:TAXI}")
  private String providerType;

  @Autowired
  private FleetSimulatorProperties props;

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  @Autowired
  private TripService tripService;

  @Autowired
  private GeoGoogleService geoService;

  @Autowired
  private VehicleEsRepository vehicleEsRepo;

  @Autowired
  private ProviderEsRepository providerEsRepo;

  @Autowired
  private TripEsRepository tripEsRepo;

  @Autowired
  private JourneyEsRepository journeyEsRepo;

  @Autowired(required = false)
  private GtfsTripJpaRepository tripJpaRepo;

  @Autowired(required = false)
  private GtfsStopTimeJpaRepository stopTimeJpaRepo;

  // @Scheduled(initialDelayString = "10000")
  @PostConstruct
  public void init() {
    if (props.getElasticsearch().isCreateIndex()) {
      if (!elasticsearchTemplate.indexExists(Journey.class)) {
        elasticsearchTemplate.createIndex(Journey.class);
      }
      if (!elasticsearchTemplate.indexExists(Trip.class)) {
        elasticsearchTemplate.createIndex(Trip.class);
      }
      if (!elasticsearchTemplate.indexExists(Vehicle.class)) {
        elasticsearchTemplate.createIndex(Vehicle.class);
      }
      if (!elasticsearchTemplate.indexExists(Provider.class)) {
        elasticsearchTemplate.createIndex(Provider.class);
      }
    }

    Provider provider = generateProvider();

    switch (props.getType()) {
      case TAXI:
        List<Vehicle> vehicles = generateVehicles();
        List<Trip> trips = generateTrips(vehicles.get(vehicles.size() - 1));
        List<Journey> journeys = generateJourneys(trips);
        break;
      case BUS:
        if (props.getGtfs().isEnabled()) {
          // gtfsLoader.loadGtfs();
          Provider busProvider = provider;
          if (props.getGtfs().isGenerate()) {
            List<Journey> gtfsJourneys = generateGtfsJourneys(busProvider);
          }
        }
        break;
      default:
        throw new IllegalStateException("");
    }
  }

  private Provider generateProvider() {
    if (props.getElasticsearch().isEraseData()
        && elasticsearchTemplate.indexExists(Provider.class)) {
      providerEsRepo.deleteAll();
    }

    return providerEsRepo.save(Provider.builder().providerId(providerId).name(providerName)
        .type(ProviderType.valueOf(providerType.toUpperCase())).build());
  }

  private List<Vehicle> generateVehicles() {
    if (props.getElasticsearch().isEraseData()
        && elasticsearchTemplate.indexExists(Vehicle.class)) {
      vehicleEsRepo.deleteAll();
    }
    return generateStaticTaxis();
  }

  private List<Trip> generateTrips(Vehicle vehicle) {
    if (props.getElasticsearch().isEraseData() && elasticsearchTemplate.indexExists(Trip.class)) {
      tripEsRepo.deleteAll();
    }
    return Collections.emptyList();

    // return asList(tripEsRepo.save(Trip.builder().description("Dummy Trip")
    // .position(Position.builder().lat(-36.851243).lng(174.777399).build())
    // .from(Position.builder().lat(-36.851243).lng(174.777399).build())
    // .to(Position.builder().lat(-36.869982).lng(174.777591).build()).providerId(providerId)
    // .route(geoService.getRouteFromTo(Position.builder().lat(-36.851243).lng(174.777399).build(),
    // Position.builder().lat(-36.869982).lng(174.777591).build()).orElse(null))
    // .vehicleId(vehicle.getVehicleId()).start(now()).status(OUT_OF_SERVICE)
    // .eta(now().plusHours(1)).status(ENROUTE).build()));
  }

  private List<Journey> generateJourneys(List<Trip> trips) {
    if (props.getElasticsearch().isEraseData()
        && elasticsearchTemplate.indexExists(Journey.class)) {
      journeyEsRepo.deleteAll();
    }
    return Collections.emptyList();
    // return asList(
    // journeyEsRepo.save(Journey.builder().description("Dummy Journey").providerId(providerId)
    // .tripIds(trips.stream().map(t -> t.getTripId()).collect(toList())).build()));
  }

  private List<Vehicle> generateStaticTaxis() {
    Provider provider = providerEsRepo.findAll().iterator().next();
    int numberOfTaxis = props.getNumberOfTaxis();
    List<Vehicle> vehicles = new ArrayList<>();
    for (int i = 0; i < numberOfTaxis; i++) {
      vehicles.add(Vehicle.builder().status(AVAILABLE).licencePlate("CAB " + i)
          .description("Taxi is available, Driver is having a nap").speed(0.0)
          .providerId(provider.getProviderId()).vehicleType(TAXI).position(Position.builder()
              .lat(-36.851243 + (i * (0.00002))).lng(174.777399 + (i * (0.00002))).build())
          .status(AVAILABLE).build());
    }
    return (List<Vehicle>) vehicleEsRepo.saveAll(vehicles);
  }

  private List<Journey> generateGtfsJourneys(Provider provider) {
    final List<Journey> journeys = new ArrayList<>();
    tripJpaRepo.findAll().stream().forEach(t -> {
      Route r = t.getRoute();
      String routeId = r.getRouteId();
      LocalTime timeNow = LocalTime.now();
      List<StopTime> stopTimes =
          stopTimeJpaRepo.findByTrip_TripIdOrderByStopSequenceAsc(t.getTripId());
      StopTime first = stopTimes.stream().findFirst().orElse(null);
      StopTime last = stopTimes.stream().reduce((a, b) -> b).orElse(null);
      if (first != null && last != null && last.getArrivalTime() != null
          && first.getDepartureTime() != null
          && last.getArrivalTime().isAfter(first.getDepartureTime())) {
        if (first.getDepartureTime().isBefore(timeNow)) {
          if (last.getArrivalTime().isBefore(timeNow)) {
            log.info("Trip is already over");
            // Nothing to do
          } else {
            log.info("Trip already started; Now find Position on map");
            // Check if trip is already existing as journey in ES
            Optional<Journey> existingJourney =
                journeyEsRepo.findFirstByGtfsTripIdAndGtfsRouteId(t.getTripId(), r.getRouteId());
            if (!existingJourney.isPresent()) {
              log.trace("There is a GTFS Trip that is not in present in ES yet");
              StopTime currentStop = stopTimes.stream()
                  .filter(st -> st.getDepartureTime().isAfter(timeNow)).findFirst().orElse(null);
              if (currentStop != null) {
                log.trace("Found Stop in current Time range");
                List<Stop> stops =
                    stopTimes.stream().filter(st -> st.getDepartureTime().isAfter(timeNow))
                        .map(st -> st.getStop()).collect(toList());
                PositionBuilder positionBuilder = Position.builder();
                if (currentStop.getStop() != null) {
                  positionBuilder.lat(currentStop.getStop().getLatitude())
                      .lng(currentStop.getStop().getLongitude())
                      .address(currentStop.getStop().getName());
                }
                final Position position = positionBuilder.build();
                final List<Trip> tripsEs = new ArrayList<>();

                Vehicle vehicle = generateGtfsVehicle(provider, r, position);
                stops.forEach(stop -> {
                  Position newPosition = Position.builder().lat(stop.getLatitude())
                      .lng(stop.getLongitude()).address(currentStop.getStop().getName()).build();

                  Trip trip =
                      Trip.builder().from(position).to(newPosition).vehicle(vehicle).build();
                  // if (CollectionUtils.isEmpty(t.getShapes())) {
                  // DirectionsRoute route = new DirectionsRoute();
                  // route.copyrights = "[{" + t.getShapes().stream()
                  // .map(sh -> "'lat': " + sh.getLatitude() + ", 'lng': " + sh.getLongitude())
                  // .collect(joining("},{")) + "}]";
                  // trip.setRoute(route);
                  // }
                  trip = tripService.bookTrip(trip);
                  trip.setDescription("Bus on Route " + r.getShortName() + " [" + t.getHeadsign()
                      + "] \n Stop Sequence: " + currentStop.getDepartureTime() + ": "
                      + stop.getName());
                  tripsEs.add(trip);
                  position.setLat(newPosition.getLat());
                  position.setLng(newPosition.getLng());
                });
                vehicle.setTrips(tripsEs.stream()
                    .collect(Collectors.toMap(Trip::getTripId, Function.identity())));
                vehicleEsRepo.save(vehicle);
                List<Trip> tripsEs2 = (List<Trip>) tripEsRepo.saveAll(tripsEs);
                journeys
                    .add(
                        journeyEsRepo.save(Journey.builder().active(true).available(true)
                            .bookingId(t.getTripId() + System.currentTimeMillis())
                            .name(r.getShortName()).provider(provider)
                            .passenger(Passenger.builder().name("Test Dummy").build())
                            .gtfsRouteId(r.getRouteId()).gtfsTripId(t.getTripId()).status(ENROUTE)
                            .from(position).to(tripsEs2.stream().reduce((a, b) -> b).get().getTo())
                            .description("Agency: " + r.getAgency().getName() + " | "
                                + r.getShortName() + " | "
                                + stops.stream().map(st -> st.getName())
                                    .collect(Collectors.joining(" -> ")))
                            .tripIds(tripsEs2.stream().map(t2 -> t2.getTripId()).collect(toList()))
                            .vehicle(vehicle).status(ENROUTE).build()));
              } else {
                log.info("Current Stop is null. How can that be?");
              }
            } else {
              log.info("GTFS Trip is alread saved as Journey in ES");
              // Nothing to do
            }
          }
        } else {
          log.info("Trip is in the future");
          // Nothing to do
        }
      } else {
        log.info("No valid Arrival or Departure Dates");
        // Cannot do anything
      }
    });
    return journeys;
  }

  private Vehicle generateGtfsVehicle(Provider provider, Route route, Position position) {
    return vehicleEsRepo.save(
        Vehicle.builder().status(ENROUTE).licencePlate(getRandomLicencePlateStartingWith("BUS"))
            .description("Bus " + route.getRouteId() + " " + route.getShortName()).speed(50.0)
            .providerId(provider.getProviderId()).vehicleType(VehicleType.BUS).position(position)
            .build());
  }

  @Transactional
  private void generateRandomVehicle(boolean lazy) {
    List<Vehicle> vehicles = ((Page<Vehicle>) vehicleEsRepo.findAll()).getContent();

    if (isEmpty(vehicles)) {
      final List<Vehicle> taxis = new ArrayList<>();
      List<Position> randomPositions = geoService.getRandomPosition(props.getCity(),
          props.getCircuit(), props.getNumberOfTaxis());

      randomPositions.forEach(latLng -> {
        Position position = new Position();
        BeanUtils.copyProperties(latLng, position);
        taxis.add(Vehicle.builder().status(AVAILABLE).licencePlate(getRandomLicencePlate())
            .position(position).build());
      });

      vehicleEsRepo.saveAll(taxis);
    }
  }

  private String getRandomLicencePlateStartingWith(String start) {
    final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String numbers = "0123456789";
    StringBuilder licencePlate = new StringBuilder(start);

    int alphaLength = 3 - start.length();
    Random r = new Random();
    for (int i = 0; i < alphaLength; i++) {
      licencePlate.append(alphabet.charAt(r.nextInt(alphabet.length())));
    }
    int numLength = 3;
    for (int i = 0; i < numLength; i++) {
      licencePlate.append(numbers.charAt(r.nextInt(numbers.length())));
    }

    return licencePlate.toString();
  }

  private String getRandomLicencePlate() {
    return getRandomLicencePlateStartingWith("");
  }

}
