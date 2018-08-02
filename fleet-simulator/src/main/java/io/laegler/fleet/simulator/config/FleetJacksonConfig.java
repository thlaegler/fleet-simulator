package io.laegler.fleet.simulator.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
import org.springframework.data.geo.GeoModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.maps.model.EncodedPolyline;
import io.laegler.fleet.simulator.geo.CustomEncodedPolyline;

@Configuration
public class FleetJacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    objectMapper.registerModule(new GeoModule());
    objectMapper.registerModule(new CustomGeoModule());
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.addMixIn(EncodedPolyline.class, CustomEncodedPolyline.class);

    return objectMapper;
  }

}
