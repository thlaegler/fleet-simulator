package io.laegler.fleet.simulator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import io.laegler.fleet.model.ProviderMode;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = "fleet-simulator")
public class FleetSimulatorProperties {

  private String id;

  private String name;

  private ProviderMode mode;

  private ProviderType type;

  private String city;

  private double circuit;

  private int numberOfTaxis;

  private int allowedCircuit;

  private String geoFireDatabase;

  private String geoFireRef;

  private FleetSimulatorElasticsearchProperties elasticsearch;

  private FleetSimulatorGtfsProperties gtfs;

  private FleetSimulatorGoogleProperties google;

  private FleetSimulatorKafkaProperties kafka;

}
