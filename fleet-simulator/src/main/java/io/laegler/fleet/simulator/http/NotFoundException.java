package io.laegler.fleet.simulator.http;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = NOT_FOUND)
public class NotFoundException extends RuntimeException {

  private static final long serialVersionUID = 5971118140424852987L;

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(Exception ex) {
    super(ex);
  }

}
