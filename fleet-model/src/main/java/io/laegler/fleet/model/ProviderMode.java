package io.laegler.fleet.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ProviderMode {

  @JsonEnumDefaultValue
  UNKNOWN, //
  RANDOM_AUTO, STATIC_AUTO, STATIC;

}
