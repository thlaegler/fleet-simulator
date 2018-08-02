package io.laegler.fleet.simulator.geo;

import com.fasterxml.jackson.annotation.JsonCreator;

public abstract class CustomEncodedPolyline {

  @JsonCreator
  CustomEncodedPolyline() {}

  @JsonCreator
  CustomEncodedPolyline(String encodedPoints) {}

}
