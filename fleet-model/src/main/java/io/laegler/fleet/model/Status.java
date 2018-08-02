package io.laegler.fleet.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {

  @JsonEnumDefaultValue
  UNKNOWN, //
  AVAILABLE, REQUEST, SCHEDULED, BOOKED, PICKUP, ENROUTE, FINISHED, CANCELLED, INVALID, OUT_OF_SERVICE, SERVER_ERROR, DISPATCHING;

  public static Status fromString(String value) {
    for (Status grade : values()) {
      if (grade.name().equalsIgnoreCase(value)) {
        return grade;
      }
    }
    return UNKNOWN;
  }

}
