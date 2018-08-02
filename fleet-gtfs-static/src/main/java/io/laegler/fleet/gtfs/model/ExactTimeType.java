package io.laegler.fleet.gtfs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * exact_times Optional The exact_times field determines if frequency-based trips should be exactly
 * scheduled based on the specified headway information.<br>
 * <br>
 * The value of exact_times must be the same for all frequencies.txt rows with the same trip_id. If
 * exact_times is 1 and a frequencies.txt row has a start_time equal to end_time, no trip must be
 * scheduled. When exact_times is 1, care must be taken to choose an end_time value that is greater
 * than the last desired trip start time but less than the last desired trip start time +
 * headway_secs.
 *
 * Valid values for code are:
 *
 * <pre>
 * 0 or (empty) - Frequency-based trips are not exactly scheduled. This is the default behavior.
 * 1 - Frequency-based trips are exactly scheduled. For a frequencies.txt row, trips are scheduled
 *  starting with trip_start_time = start_time + x * headway_secs for all x in (0, 1, 2, ...)
 *  where trip_start_time lower than end_time.
 * </pre>
 */
@AllArgsConstructor
public enum ExactTimeType {
  NOT_EXACT(0), EXACT(1);

  @Getter
  private final int code;

  public static ExactTimeType fromCode(Integer code) {
    if (code != null) {
      for (ExactTimeType e : ExactTimeType.values()) {
        if (e.getCode() == code.intValue()) {
          return e;
        }
      }
    }
    return NOT_EXACT;
  }
}
