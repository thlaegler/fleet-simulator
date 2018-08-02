// package io.laegler.fleet.simulator.config;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import com.google.maps.GeoApiContext;
//
// @Configuration
// public class GoogleApiConfig {
//
// @Value("${fleet-simulator.google.serverKey}")
// private String googleApiKey;
//
// @Bean
// public GeoApiContext geoApiContext() {
// return new GeoApiContext.Builder().apiKey(googleApiKey).build();
// }
//
// }
