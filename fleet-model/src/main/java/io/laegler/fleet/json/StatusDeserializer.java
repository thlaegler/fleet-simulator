package io.laegler.fleet.json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.laegler.fleet.model.Status;

public class StatusDeserializer extends StdDeserializer<Status> {

  private static final long serialVersionUID = -7511281862230177354L;

  public StatusDeserializer() {
    this(null);
  }

  public StatusDeserializer(Class<Status> t) {
    super(t);
  }

  @Override
  public Status deserialize(JsonParser parser, DeserializationContext context)
      throws IOException, JsonProcessingException {
    return Status.fromString(parser.getValueAsString());
  }

}
