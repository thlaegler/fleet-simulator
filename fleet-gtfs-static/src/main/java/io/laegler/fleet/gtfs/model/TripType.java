package io.laegler.fleet.gtfs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The wheelchair_boarding field identifies whether wheelchair boardings are possible from the
 * specified stop / station, trips or routes.
 */
@AllArgsConstructor
public enum TripType {

  NO_INFO(0), AVAILABLE_AT_SOME_VEHICLES(1), NOT_AVAILABLE(2), UNKNOWN(3);

  @Getter
  private final int code;

  public static TripType fromCode(Integer code) {
    if (code != null) {
      for (TripType e : TripType.values()) {
        if (e.getCode() == code.intValue()) {
          return e;
        }
      }
    }
    return NO_INFO;
  }

}
