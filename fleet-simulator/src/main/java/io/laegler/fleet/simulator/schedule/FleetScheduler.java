package io.laegler.fleet.simulator.schedule;

import static io.laegler.fleet.model.Status.AVAILABLE;
import static io.laegler.fleet.model.Status.ENROUTE;
import static io.laegler.fleet.model.Status.FINISHED;
import static io.laegler.fleet.model.Status.PICKUP;
import static java.time.ZoneOffset.UTC;
import static org.springframework.util.CollectionUtils.isEmpty;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsStep;
import io.laegler.fleet.model.Journey;
import io.laegler.fleet.model.Position;
import io.laegler.fleet.model.Trip;
import io.laegler.fleet.model.Vehicle;
import io.laegler.fleet.repo.TripEsRepository;
import io.laegler.fleet.repo.VehicleEsRepository;
import io.laegler.fleet.simulator.service.JourneyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FleetScheduler {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private NotifyService notifyService;

  @Autowired
  private JourneyService journeyService;

  @Autowired
  private TripEsRepository tripRepo;

  @Autowired
  private VehicleEsRepository vehicleRepo;

  @Scheduled(fixedDelayString = "${fleet-simulator.tick:5000}") // 5 seconds
  public void moveFleet() {
    log.info(" +++ SCHEDULER TRIGGERED +++ ");

    moveJourneys();
  }

  public void moveJourneys() {
    List<Journey> journeys = journeyService.getActiveJourneys();
    journeys.stream().forEach(j -> {
      Vehicle vehicle = j.getTrips().values().stream().map(t -> moveTrip(t)).findFirst()
          .orElseThrow(() -> new IllegalStateException("Cannot find Vehicle for Trip"));
      j.setVehicle(vehicle);
      j.setPosition(vehicle.getPosition());
      if (!isEmpty(j.getTrips())) {
        tripRepo.saveAll(j.getTrips().values())
            .forEach(t -> notifyService.notify("trip", t.getTripId()));
      }
      j.setTimestampUTC(System.currentTimeMillis());
    });
    if (!isEmpty(journeys)) {
      journeyService.saveJourneys(journeys)
          .forEach(j -> notifyService.notify("journey", j.getBookingId()));
    }
  }

  private Vehicle moveTrip(Trip trip) {
    Vehicle vehicle = vehicleRepo.findById(trip.getVehicleId())
        .orElseThrow(() -> new IllegalStateException("Cannot have Trip without Vehicle"));

    // Is Trip at the end?
    if (trip.getStatus() != null && trip.getStatus().equals(FINISHED)) {
      return vehicle;
    }
    if (trip.getPosition().equals(trip.getTo())) {
      if (trip.getStatus() != null && trip.getStatus().equals(PICKUP)) {
        Trip nextTrip = tripRepo
            .findById(journeyService.getJourneyByTripId(trip.getTripId()).getTripIds().stream()
                .filter(t -> !t.toString().equals(trip.getTripId().toString())).findFirst().get())
            .get();
        nextTrip.setStatus(ENROUTE);
        nextTrip.setTimestampUTC(System.currentTimeMillis());
        tripRepo.save(nextTrip);
        notifyService.notify("trip", nextTrip.getTripId());
      } else {
        vehicle.setStatus(AVAILABLE);
        vehicle.setDescription("Just dropped of a passenger and is now available");
      }
      trip.setStatus(FINISHED);
    } else {
      Position newPosition = calculateNewPosition(trip);
      trip.setPosition(newPosition);
      vehicle.setPosition(newPosition);
    }

    vehicle.setTimestampUTC(System.currentTimeMillis());
    notifyService.notify("vehicle", vehicle.getVehicleId());
    return vehicleRepo.save(vehicle);
  }

  private Position calculateNewPosition(Trip trip) {
    LocalDateTime now = LocalDateTime.now();
    long pastSeconds = 0L;
    long startSecond = trip.getStart().atOffset(UTC).toEpochSecond();
    long currentSeconds = now.atOffset(UTC).toEpochSecond() - startSecond;
    long endSeconds = trip.getEta().atOffset(UTC).toEpochSecond() - startSecond;
    Position newLocation = trip.getPosition();

    int i = 0;
    if (trip.getRoute() != null && trip.getStart().isBefore(now)) {
      if (trip.getEta().isAfter(now)) {
        return trip.getTo();
      }
      List<Position> positions = new ArrayList<>();
      try {
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        positions = objectMapper.readValue(trip.getRoute().copyrights,
            new TypeReference<List<Position>>() {});
      } catch (JsonParseException ex1) {
        log.error("Cannot parse polyline", ex1);
      } catch (JsonMappingException ex2) {
        log.error("Cannot parse polyline", ex2);
      } catch (IOException ex3) {
        log.error("Cannot parse polyline", ex3);
      }
      for (DirectionsLeg directionsLeg : trip.getRoute().legs) {
        for (DirectionsStep step : directionsLeg.steps) {
          Long beforeSeconds = pastSeconds;
          pastSeconds = pastSeconds + step.duration.inSeconds;
          if (beforeSeconds < currentSeconds && currentSeconds <= pastSeconds) {
            if (currentSeconds + 10 < pastSeconds && !isEmpty(positions)) {
              Position legStartPosition = positions.stream()
                  .filter(p -> round(p.getLat(), 5) == round(step.startLocation.lat, 5)
                      && round(p.getLng(), 5) == round(step.startLocation.lng, 5))
                  .findFirst().orElse(null);
              if (legStartPosition != null) {
                int legStartIndex = positions.indexOf(legStartPosition);
                Long nextIndexLong = legStartIndex + (currentSeconds / 15);
                Integer nextIndex = nextIndexLong.intValue();
                if (nextIndex <= positions.size()) {
                  return positions.get(nextIndex);
                } else {
                  return positions.get(positions.size() - 1);
                }
              } else {
                return Position.builder().lat(step.endLocation.lat).lng(step.endLocation.lng)
                    .build();
              }
            } else {
              return Position.builder().lat(step.endLocation.lat).lng(step.endLocation.lng).build();
            }
          }
        }
      }
    }
    return trip.getPosition();
  }

  public static double round(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

}
