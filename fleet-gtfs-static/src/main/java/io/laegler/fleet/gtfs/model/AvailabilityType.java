package io.laegler.fleet.gtfs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * drop_off_type / pick_up_type Optional The drop_off_type / pick_up_type field indicates whether
 * passengers are dropped off / pick up at a stop as part of the normal schedule or whether a drop
 * off at the stop is not available. This field also allows the transit agency to indicate that
 * passengers must call the agency or notify the driver to arrange a drop off / pick up at a
 * particular stop.
 *
 * Valid values for code are:
 *
 * <pre>
 *     0 - Regularly scheduled drop off / pick up
 *     1 - No drop off / pick up available
 *     2 - Must phone agency to arrange drop off / pick up
 *     3 - Must coordinate with driver to arrange drop off / pick up
 * </pre>
 */
@AllArgsConstructor
public enum AvailabilityType {
  REGULAR(0), NOT_AVAILABLE(1), MUST_PHONE_AGENCY(2), MUST_COORDINATE_WITH_DRIVER(3), UNKNOWN(4);

  @Getter
  private final int code;

  public static AvailabilityType fromCode(Integer code) {
    if (code != null) {
      for (AvailabilityType e : AvailabilityType.values()) {
        if (e.getCode() == code.intValue()) {
          return e;
        }
      }
    }
    return REGULAR;
  }

}
