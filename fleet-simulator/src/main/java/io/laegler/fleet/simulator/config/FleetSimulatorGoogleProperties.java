package io.laegler.fleet.simulator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = "fleet-simulator.google")
public class FleetSimulatorGoogleProperties {

  private String serverKey;

  private String apiKey;

  private String browserKey;

}
