package io.laegler.fleet.simulator.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
// @SpringBootTest
public class VehicleServiceTest {

  @Autowired
  private VehicleService taxiService;

  @Test
  public void whenICheckAvailability_thenProviderShouldBeAvailable() {
    // Given
    // doReturn(new ArrayList<PlacesSearchResult>()).when(geoService)
    // .getNearByPlacesByLatlng(any(LatLng.class));
    // When
    // Journey resp = taxiService.getClosestAvailableVehicle(center)checkAvailabily(
    // Journey.builder().from(Position.builder().placeId("ChIJoUPB3bxHDW0RkMiiQ2HvAAU").build())
    // .to(Position.builder().placeId("ChIJ3abyGptHDW0REPmiQ2HvAAU").build()).build());
    // // Then
    // assertTrue(resp.isAvailable());
  }

  @Test
  public void whenICheckAvailabilityOutOfRange_thenProviderShouldNotBeAvailable() {
    // Given
    // doReturn(new ArrayList<PlacesSearchResult>()).when(geoService)
    // .getNearByPlacesByLatlng(any(LatLng.class));
    // When
    // AvailabilityResponse resp = taxiService.checkProviderAvailabily(
    // AvailabilityRequest.builder().from(Position.builder().lat(66.666).lng(66.666).build())
    // .to(Position.builder().lat(-66.666).lng(66.666).build()).build());
    // // Then
    // assertFalse(resp.isAvailable());
  }

}
