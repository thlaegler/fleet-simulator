package io.laegler.fleet.simulator.http;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@JsonInclude(NON_NULL)
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class FleetErrorMessage {

  @Getter
  private final String message;

  @Getter
  @Setter
  private String reason;

}
