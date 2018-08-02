package io.laegler.fleet.simulator.http;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = BAD_REQUEST)
public class InternalServerErrorException extends RuntimeException {

  private static final long serialVersionUID = 2537387962609158289L;

  public InternalServerErrorException(String message) {
    super(message);
  }

  public InternalServerErrorException(Exception ex) {
    super(ex);
  }

}
