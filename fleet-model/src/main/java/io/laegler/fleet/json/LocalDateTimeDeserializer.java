package io.laegler.fleet.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

  private static final long serialVersionUID = -7511281862230177354L;

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

  public LocalDateTimeDeserializer() {
    this(null);
  }

  public LocalDateTimeDeserializer(Class<LocalDateTime> t) {
    super(t);
  }

  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {

    JsonNode node = parser.getCodec().readTree(parser);
    String dateTimeString = node.asText();
    return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
  }
}
