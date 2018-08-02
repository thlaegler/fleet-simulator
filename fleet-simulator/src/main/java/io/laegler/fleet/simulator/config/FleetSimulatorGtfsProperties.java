package io.laegler.fleet.simulator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = "fleet-simulator.gtfs")
public class FleetSimulatorGtfsProperties {

  private boolean enabled;

  private boolean generate;

}
