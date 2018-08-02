package io.laegler.fleet.simulator.http;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = BAD_REQUEST)
public class OutOfRangeException extends RuntimeException {

  private static final long serialVersionUID = 5971118140424852987L;

  public OutOfRangeException(String message) {
    super(message);
  }

  public OutOfRangeException(Exception ex) {
    super(ex);
  }

}
