package io.laegler.fleet.simulator.geo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.maps.model.EncodedPolyline;
import java.io.IOException;

public class EncodedPolylineDeserializer extends JsonDeserializer<EncodedPolyline> {

  @Override
  public EncodedPolyline deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    return new EncodedPolyline(jp.getText());
  }

}

