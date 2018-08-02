package io.laegler.fleet.gtfs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The transfer_type field specifies the type of connection for the specified (from_stop_id,
 * to_stop_id) pair.
 *
 * Valid values for code are:
 *
 * <pre>
 * 0 or (empty) - This is a recommended transfer point between two routes.
 * 1 - This is a timed transfer point between two routes. The departing vehicle is expected
 * to wait for the arriving one, with sufficient time for a passenger to transfer between routes.
 * 2 - This transfer requires a minimum amount of time between arrival and departure to ensure
 * a connection. The time required to transfer is specified by min_transfer_time.
 * 3 - Transfers are not possible between routes at this location.
 * </pre>
 */
@AllArgsConstructor
public enum TransferType {
  RECOMMENDED(0), TRANSFER_WILL_WAIT(1), REQUIRE_MIN_TIME(2), NOT_POSSIBLE(3), UNKNOWN(2);

  @Getter
  private final int code;

  public static TransferType fromCode(Integer code) {
    if (code != null) {
      for (TransferType e : TransferType.values()) {
        if (e.getCode() == code.intValue()) {
          return e;
        }
      }
    }
    return NOT_POSSIBLE;
  }
}
