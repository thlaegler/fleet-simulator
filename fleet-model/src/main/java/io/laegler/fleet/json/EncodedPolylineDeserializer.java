package io.laegler.fleet.json;

import java.io.IOException;
import java.time.LocalDateTime;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.maps.model.EncodedPolyline;

public class EncodedPolylineDeserializer extends StdDeserializer<EncodedPolyline> {

  private static final long serialVersionUID = -7511281862230177354L;

  public EncodedPolylineDeserializer() {
    this(null);
  }

  public EncodedPolylineDeserializer(Class<LocalDateTime> t) {
    super(t);
  }

  @Override
  public EncodedPolyline deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = parser.getCodec().readTree(parser);
    String polylineString = node.asText();
    return new EncodedPolyline(polylineString);
  }
}
