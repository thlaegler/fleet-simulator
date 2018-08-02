package io.laegler.fleet.gtfs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The transfers field specifies the number of transfers permitted on this fare.
 *
 * Valid values for code are:
 * 
 * <pre>
 * 0 - No transfers permitted on this fare.
 * 1 - Passenger may transfer once.
 * 2 - Passenger may transfer twice.
 * (empty) - If this field is empty, unlimited transfers are permitted.
 * </pre>
 */
@AllArgsConstructor
public enum FareTransferType {

  NOT_ALLOWED(0), //
  ONCE(1), //
  TWICE(2), //
  UNLIMITED(-1), UNKNOWN(3);

  @Getter
  private final int code;

  public static FareTransferType fromCode(String code) {
    int internalCode = -1;
    if ((code != null) && !"".equals(code)) {
      internalCode = Integer.parseInt(code);
    }
    for (FareTransferType e : FareTransferType.values()) {
      if (e.getCode() == internalCode) {
        return e;
      }
    }
    return UNKNOWN;
  }
}
