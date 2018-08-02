package io.laegler.fleet.simulator.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import java.io.IOException;
import java.util.Properties;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.geo.GeoModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.maps.model.EncodedPolyline;
import io.laegler.fleet.simulator.geo.CustomEncodedPolyline;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = "io.laegler.fleet.repo")
public class ElasticsearchConfig {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ElasticsearchProperties elasticsearchProperties;

  @Bean
  public ElasticsearchTemplate elasticsearchTemplate() {
    try {
      return new ElasticsearchTemplate(elasticsearchClient(),
          new ElasticsearchEntityMapper(objectMapper));
    } catch (Exception e) {
      log.error("Failed to create Elasticsearch Client Template", e);
    }
    return null;
  }

  @Bean
  public TransportClient elasticsearchClient() {
    System.setProperty("es.set.netty.runtime.available.processors", "false");
    TransportClientFactoryBean factory = new TransportClientFactoryBean();
    factory.setClusterName(elasticsearchProperties.getClusterName());
    factory.setClusterNodes(elasticsearchProperties.getClusterNodes());
    factory.setProperties(createProperties());
    try {
      factory.afterPropertiesSet();
      TransportClient client = factory.getObject();
      return client;
    } catch (Exception e) {
      new IllegalStateException("Cannot create Elasticsearch Client Template");
    }
    return null;
  }

  private Properties createProperties() {
    Properties properties = new Properties();
    properties.put("cluster.name", elasticsearchProperties.getClusterName());
    return properties;
  }

  public static class ElasticsearchEntityMapper implements EntityMapper {

    private ObjectMapper objectMapper;

    public ElasticsearchEntityMapper(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
      objectMapper = jackson2ObjectMapperBuilder.createXmlMapper(false).build();

      // objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, true);
      objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
      objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
      objectMapper.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
      objectMapper.registerModule(new GeoModule());
      objectMapper.registerModule(new CustomGeoModule());
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.addMixIn(EncodedPolyline.class, CustomEncodedPolyline.class);
    }

    public ElasticsearchEntityMapper(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
    }

    @Override
    public String mapToString(Object object) throws IOException {
      return objectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
      return objectMapper.readValue(source, clazz);
    }
  }
}
