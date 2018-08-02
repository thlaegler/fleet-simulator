package io.laegler.fleet.gtfs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * direction_id Optional The direction_id field contains a binary value that indicates the direction
 * of travel for a trip. Use this field to distinguish between bi-directional trips with the same
 * route_id. This field is not used in routing; it provides a way to separate trips by direction
 * when publishing time tables. You can specify names for each direction with the trip_headsign
 * field.
 *
 * It provides a way to separate trips by direction when publishing time tables. You can specify
 * names for each direction with the trip_headsign field.
 *
 * <pre>
 *     0 - travel in one direction (e.g. outbound travel)
 *     1 - travel in the opposite direction (e.g. inbound travel)
 * </pre>
 */
@AllArgsConstructor
public enum DirectionType {
  INBOUND(0), OUTBOUND(1), UNKNOWN(2);

  @Getter
  private final int code;

  public static DirectionType fromCode(Integer code) {
    if (code != null) {
      for (DirectionType e : DirectionType.values()) {
        if (e.getCode() == code.intValue()) {
          return e;
        }
      }
    }
    return INBOUND;
  }

}
