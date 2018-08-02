package io.laegler.fleet.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ProviderProtocol {

  @JsonEnumDefaultValue
  UNKNOWN, //
  WEBSOCKET, SOAP, REST, GRPC;
}
