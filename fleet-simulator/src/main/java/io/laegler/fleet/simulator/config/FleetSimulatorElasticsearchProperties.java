package io.laegler.fleet.simulator.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = "fleet-simulator.elasticsearch")
public class FleetSimulatorElasticsearchProperties {

  private boolean eraseData;

  private boolean createIndex;

  private Map<String, String> journey;

  private Map<String, String> trip;

  private Map<String, String> vehicle;

  private Map<String, String> provider;

}
