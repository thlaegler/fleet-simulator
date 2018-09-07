package io.laegler.fleet.gtfs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * exception_type Required The exception_type indicates whether service is available on the date
 * specified in the date field.
 *
 * Corresponding code to service exception.
 *
 * <pre>
 *     A value of 1 indicates that service has been added for the specified date.
 *     A value of 2 indicates that service has been removed for the specified date.
 * </pre>
 *
 * For example, suppose a route has one set of trips available on holidays and another set of trips
 * available on all other days. You could have one service_id that corresponds to the regular
 * service schedule and another service_id that corresponds to the holiday schedule. For a
 * particular holiday, you would use the calendar_dates.txt file to add the holiday to the holiday
 * service_id and to remove the holiday from the regular service_id schedule.
 */
@AllArgsConstructor
public enum ExceptionType {
  ADDED_FOR_SPECIFIC_DATE(1), REMOVED_FOR_SPECIFIC_DATE(2), UNKNOWN(8);

  @Getter
  private final int code;

  public static ExceptionType fromCode(Integer code) {
    if (code != null) {
      for (ExceptionType e : ExceptionType.values()) {
        if (e.getCode() == code.intValue()) {
          return e;
        }
      }
    }
    return UNKNOWN;
  }

}
