package io.laegler.fleet.simulator.geo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.maps.model.EncodedPolyline;
import java.io.IOException;

public class EncodedPolylineSerializer extends JsonSerializer<EncodedPolyline> {

  @Override
  public void serialize(EncodedPolyline value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
    jgen.writeString(value.getEncodedPath());
  }

}

