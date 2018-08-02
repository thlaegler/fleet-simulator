package io.laegler.fleet.gtfs.model;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.laegler.fleet.gtfs.GtfsLocalTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Headway (time between trips) for routes with variable frequency of service.<br>
 * <br>
 * This table is intended to represent schedules that don't have a fixed list of stop times. When
 * trips are defined in frequencies.txt, the trip planner ignores the absolute values of the
 * arrival_time and departure_time fields for those trips in stop_times.txt. Instead, the stop_times
 * table defines the sequence of stops and the time difference between each stop.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "frequencies")
public class Frequency {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long frequencyId;

  @ManyToOne
  @JoinColumn(name = "trip_id", nullable = false)
  private Trip trip;

  @JsonSerialize(using = LocalTimeSerializer.class)
  @JsonDeserialize(using = LocalTimeDeserializer.class)
  @Convert(converter = GtfsLocalTimeConverter.class)
  @Column(name = "start_time", columnDefinition = "varchar(255)", nullable = false)
  private LocalTime startTime;

  @JsonSerialize(using = LocalTimeSerializer.class)
  @JsonDeserialize(using = LocalTimeDeserializer.class)
  @Convert(converter = GtfsLocalTimeConverter.class)
  @Column(name = "end_time", columnDefinition = "varchar(255)", nullable = false)
  private LocalTime endTime;

  @Column(name = "headway_secs")
  private Long headwaySecs;

  @Column(name = "exact_time")
  private ExactTimeType exactTime;
}
