package io.laegler.fleet.simulator.geo;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.DirectionsRoute;
import java.io.IOException;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Converter(autoApply = true)
public class DirectionsRouteAttributeConverter
    implements AttributeConverter<DirectionsRoute, String> {

  @Autowired
  private ObjectMapper jsonMapper;

  @Override
  public String convertToDatabaseColumn(DirectionsRoute entityValue) {
    if (entityValue == null)
      return null;

    try {
      return jsonMapper.writeValueAsString(entityValue);
    } catch (JsonProcessingException e) {
      log.error("Cannot serialize DirectionsRoute to Database LOB/JSON");
    }
    return null;
  }

  @Override
  public DirectionsRoute convertToEntityAttribute(String databaseValue) {
    if (databaseValue == null)
      return null;

    try {
      return jsonMapper.readValue(databaseValue, DirectionsRoute.class);
    } catch (JsonParseException e) {
      log.error("Cannot parse DirectionsRoute from Database LOB/JSON");
    } catch (JsonMappingException e) {
      log.error("Cannot parse DirectionsRoute from Database LOB/JSON");
    } catch (IOException e) {
      log.error("Cannot parse DirectionsRoute from Database LOB/JSON");
    }
    return null;


  }
}
