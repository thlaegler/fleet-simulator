package io.laegler.fleet.simulator.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties
public class FleetSimulatorConfig {

  @Bean
  public FleetSimulatorProperties fleetSimulatorProperties() {
    return new FleetSimulatorProperties();
  }

}
