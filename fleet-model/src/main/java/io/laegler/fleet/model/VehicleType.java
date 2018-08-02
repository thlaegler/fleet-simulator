package io.laegler.fleet.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum VehicleType {

  @JsonEnumDefaultValue
  UNKNOWN, //
  TAXI, BUS, SHUTTLE, TRAIN, PLANE, FERRY, BICYCLE, CAR_SHARE

}
