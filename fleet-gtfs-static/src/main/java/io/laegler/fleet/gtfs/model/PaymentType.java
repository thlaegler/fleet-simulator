package io.laegler.fleet.gtfs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The payment_method field indicates when the fare must be paid. Valid values for code are:
 *
 * <pre>
 * 0 - Fare is paid on board.
 * 1 - Fare must be paid before boarding.
 * </pre>
 */
@AllArgsConstructor
public enum PaymentType {
  ON_BOARD(0), BEFORE_BOARD(1), UNKNOWN(2);

  @Getter
  private final int code;

  public static PaymentType fromCode(Integer code) {
    if (code != null) {
      for (PaymentType e : PaymentType.values()) {
        if (e.getCode() == code.intValue()) {
          return e;
        }
      }
    }
    return UNKNOWN;
  }
}
