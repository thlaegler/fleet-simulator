package io.laegler.fleet.simulator.service;

import static io.laegler.fleet.model.Status.AVAILABLE;
import static io.laegler.fleet.model.Status.ENROUTE;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import io.laegler.fleet.model.Driver;
import io.laegler.fleet.model.Journey;
import io.laegler.fleet.model.Position;
import io.laegler.fleet.model.Vehicle;
import io.laegler.fleet.repo.VehicleEsRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VehicleService {

  @Autowired
  private VehicleEsRepository vehicleRepo;

  @Autowired
  private VehicleCustomEsRepository vehicleCustomEsRepo;

  public Vehicle bookClosestAvailableVehicle(Journey journey) {
    Vehicle vehicle = getClosestAvailableVehicle(journey.getFrom())
        .orElseThrow(() -> new IllegalStateException("Cannot find a available Vehicle near-by"));
    vehicle.setSpeed(50.0);
    vehicle.setStatus(ENROUTE);
    vehicle.setDriver(
        Driver.builder().driverId(UUID.randomUUID().toString()).name("Timo Taxidriver").build());
    vehicle.setDescription("Taxi is going to pick up a passenger.");
    return vehicleRepo.save(vehicle);
  }

  public Optional<Vehicle> getClosestAvailableVehicle(Position center) {
    return vehicleCustomEsRepo.findByCurrentLocationAndRadiusAndAvailableOrderByDistance(center, 20)
        .stream().findFirst();
  }

  public Vehicle trackVehicle(final String vehicleId) {
    log.trace("trackVehicle({}) called", vehicleId);

    return vehicleRepo.findById(vehicleId)
        .orElseThrow(() -> new IllegalStateException("Cannot find Booking by ID: " + vehicleId));
  }

  public void cancelVehicle(String vehicleId) {
    Vehicle vehicle = vehicleRepo.findById(vehicleId).get();
    // TODO: look for next/scheduled journeys and make them earlier
    // vehicle.setStatus(PICKUP);
    vehicle.setStatus(AVAILABLE);
    vehicleRepo.save(vehicle);
  }

  // @Cacheable("allVehicles")
  public List<Vehicle> getAllVehicles() {
    log.trace("getAllVehicles() called");

    List<Vehicle> vehicles = ((Page<Vehicle>) vehicleRepo.findAll()).getContent();

    return vehicles;
  }

}

