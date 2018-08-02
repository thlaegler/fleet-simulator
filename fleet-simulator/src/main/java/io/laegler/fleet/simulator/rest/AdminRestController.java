package io.laegler.fleet.simulator.rest;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.laegler.fleet.model.Status;
import io.laegler.fleet.repo.JourneyEsRepository;
import io.laegler.fleet.repo.TripEsRepository;
import io.laegler.fleet.repo.VehicleEsRepository;
import io.laegler.fleet.simulator.service.DataGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "Fleet Administration Service", tags = "Fleet Administration Service",
    authorizations = {@Authorization(value = "OAuth2ClientCredentials"),
        @Authorization(value = "OAuth2UserPassword")})
@RestController
@RequestMapping(value = "/fleet/admin")
public class AdminRestController {

  @Autowired
  private DataGenerator dataGenerator;

  @Autowired
  private JourneyEsRepository journeyRepo;

  @Autowired
  private TripEsRepository tripRepo;

  @Autowired
  private VehicleEsRepository vehicleRepo;

  @PostMapping(value = "/reset", produces = APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({@ApiResponse(code = 204, message = "Simulation reset"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  @ApiOperation(value = "Reset the Simulation")
  public ResponseEntity<?> resetSimulation() {
    log.trace("resetSimulation({}) called");

    dataGenerator.init();

    return noContent().build();
  }

  @GetMapping(value = "/journeys", produces = APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({@ApiResponse(code = 200, message = "Got all Journeys"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  @ApiOperation(value = "Get all active Journeys")
  public ResponseEntity<?> getAllJourneys() {
    log.trace("getAllJourneys({}) called");

    return ok(journeyRepo.findByActiveTrue());
  }

  @GetMapping(value = "/trips", produces = APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({@ApiResponse(code = 200, message = "Got all Trips"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  @ApiOperation(value = "Get all active Trips")
  public ResponseEntity<?> getAllTrips() {
    log.trace("getAllTrips({}) called");

    return ok(tripRepo.findByStatusIn(asList(Status.PICKUP, Status.ENROUTE)));
  }

  @GetMapping(value = "/vehicles", produces = APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({@ApiResponse(code = 200, message = "Got all Vehicles"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  @ApiOperation(value = "Get all Vehicles")
  public ResponseEntity<?> getAllVehicles() {
    log.trace("getAllVehicles({}) called");

    return ok(vehicleRepo.findAll());
  }

}

