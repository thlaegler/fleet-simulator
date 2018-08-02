package io.laegler.fleet.simulator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = "fleet-simulator.kafka")
public class FleetSimulatorKafkaProperties {

  private boolean enabled;

}
