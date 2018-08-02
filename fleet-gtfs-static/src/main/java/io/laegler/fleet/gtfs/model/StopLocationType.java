package io.laegler.fleet.gtfs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The location_type field identifies whether this stop ID represents a stop or station. If no
 * location type is specified, or the location_type is blank, stop IDs are treated as stops.
 * Stations may have different properties from stops when they are represented on a map or used in
 * trip planning.
 *
 * The location type code can have the following values:<br>
 *
 * <pre>
 * 0 or blank - Stop. A location where passengers board or disembark from a transit vehicle.
 * 1 - Station. A physical structure or area that contains one or more stop.
 * </pre>
 */
@AllArgsConstructor
public enum StopLocationType {
  STOP(0), STATION(1), UNKNOWN(2);

  @Getter
  private final int code;

  public boolean isStation() {
    return this == STATION;
  }

  public boolean isStop() {
    return this == STOP;
  }

  public static StopLocationType fromCode(Integer code) {
    if (code != null) {
      for (StopLocationType e : StopLocationType.values()) {
        if (e.getCode() == code.intValue()) {
          return e;
        }
      }
    }
    return STOP;
  }
}
