package io.laegler.fleet.gtfs;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter(autoApply = true)
public class GtfsLocalTimeConverter implements AttributeConverter<LocalTime, String> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
  private static final DateTimeFormatter SECONDARY_FORMATTER =
      DateTimeFormatter.ofPattern("H:mm:ss");

  @Override
  public String convertToDatabaseColumn(LocalTime time) {
    return time.format(FORMATTER);
  }

  @Override
  public LocalTime convertToEntityAttribute(String timeString) {
    String[] split = timeString.split(":");

    if (timeString != null && !timeString.isEmpty() && split.length == 3) {
      try {
        int hour = Integer.parseInt(split[0]);
        if (hour >= 24) {
          hour = hour - 24;
        }
        timeString = hour + ":" + split[1] + ":" + split[2];
        return LocalTime.parse(timeString, FORMATTER);
      } catch (Exception ex1) {
        try {
          return LocalTime.parse(timeString, SECONDARY_FORMATTER);
        } catch (Exception ex2) {
          log.error("Cannot parse Time Format", ex2);
        }
      }
    }
    return null;
  }

}
