package io.laegler.fleet.simulator.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.laegler.fleet.model.Journey;
import io.laegler.fleet.simulator.service.JourneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "Fleet Journey Service", tags = "Fleet Journey Service",
    authorizations = {@Authorization(value = "OAuth2ClientCredentials"),
        @Authorization(value = "OAuth2UserPassword")})
@RestController
@RequestMapping(value = "/fleet/journeys")
public class JourneyRestController {

  @Autowired
  private JourneyService journeyService;

  @PostMapping(value = "/availability", consumes = APPLICATION_JSON_UTF8_VALUE,
      produces = APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({@ApiResponse(code = 200, message = "Availability checked"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  @ApiOperation(value = "Check if Taxi Provider is available",
      notes = "On demand Availability information; Provides transportation options for a given journey",
      response = Journey.class)
  public ResponseEntity<?> planJourney(
      @ApiParam(value = "Availability Request") @RequestBody final Journey journey) {
    log.trace("planJourney({}) called", journey);

    return ok(journeyService.planJourney(journey));
  }

  @PostMapping(value = "/book", consumes = APPLICATION_JSON_UTF8_VALUE,
      produces = APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({@ApiResponse(code = 201, message = "Journey booked"),
      @ApiResponse(code = 404, message = "Journey not available"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  @ApiOperation(value = "Book a Journey",
      notes = "Notifies the client that the booking was cancelled by the cab company.",
      response = Journey.class)
  public ResponseEntity<?> bookJourney(
      @ApiParam(value = "Booking Request") @RequestBody final Journey journey) {
    log.trace("bookJourney({}) called", journey);

    return created(null).body(journeyService.bookJourney(journey));
  }

  @DeleteMapping(value = "/cancel", produces = APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({@ApiResponse(code = 204, message = "Taxi Booking canceled"),
      @ApiResponse(code = 404, message = "Journey not available"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  @ApiOperation(value = "Cancel a booked a Journey",
      notes = "Notifies the client that the booking was cancelled by the cab company.")
  public ResponseEntity<?> cancelJourney(
      @ApiParam(value = "Cancel Booking Request") @RequestParam final String bookingId) {
    log.trace("cancelJourney(bookingId={}) called", bookingId);

    return ok(journeyService.cancelJourney(bookingId));
  }

  @GetMapping(value = "/track", produces = APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({@ApiResponse(code = 200, message = "Taxi tracked"),
      @ApiResponse(code = 404, message = "Booking not found"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  @ApiOperation(value = "Track the Status of a booked Taxi",
      notes = "Notifies the client that the changes in the cab status and/or position.",
      response = Journey.class)
  public ResponseEntity<?> trackTaxiStatus(
      @ApiParam(value = "Booking ID") @RequestParam(required = true) final String bookingId) {
    log.trace("trackTaxiStatus(bookingId={}) called", bookingId);

    return ok(journeyService.trackJourney(bookingId));
  }

}

