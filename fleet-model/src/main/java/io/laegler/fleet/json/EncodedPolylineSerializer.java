package io.laegler.fleet.json;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.maps.model.EncodedPolyline;

public class EncodedPolylineSerializer extends StdSerializer<EncodedPolyline> {

  private static final long serialVersionUID = 1001260249084871566L;

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

  public EncodedPolylineSerializer() {
    this(null);
  }

  public EncodedPolylineSerializer(Class<EncodedPolyline> t) {
    super(t);
  }

  @Override
  public void serialize(EncodedPolyline value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {

    jgen.writeString(value.getEncodedPath());
  }
}
