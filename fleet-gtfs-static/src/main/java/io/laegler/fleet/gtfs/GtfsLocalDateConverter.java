package io.laegler.fleet.gtfs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class GtfsLocalDateConverter implements AttributeConverter<LocalDate, String> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  @Override
  public String convertToDatabaseColumn(LocalDate date) {
    return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  @Override
  public LocalDate convertToEntityAttribute(String dateString) {
    if (dateString != null && !dateString.isEmpty()) {
      return LocalDate.parse(dateString, FORMATTER);
    }
    return null;
  }

}
