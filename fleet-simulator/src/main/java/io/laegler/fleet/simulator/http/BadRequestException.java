package io.laegler.fleet.simulator.http;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = BAD_REQUEST)
public class BadRequestException extends RuntimeException {

  private static final long serialVersionUID = 2537387962609158289L;

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(Exception ex) {
    super(ex);
  }

}
