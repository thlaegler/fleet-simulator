package io.laegler.fleet.gtfs;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import io.laegler.fleet.gtfs.model.RouteType;

@Converter(autoApply = true)
public class GtfsRouteTypeConverter implements AttributeConverter<RouteType, Integer> {

  @Override
  public Integer convertToDatabaseColumn(RouteType routeType) {
    return routeType.getCode();
  }

  @Override
  public RouteType convertToEntityAttribute(Integer ordinal) {
    return RouteType.fromCode(ordinal);
  }

}
