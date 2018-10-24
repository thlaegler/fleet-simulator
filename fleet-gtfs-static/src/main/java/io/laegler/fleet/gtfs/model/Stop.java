package io.laegler.fleet.gtfs.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Individual locations where vehicles pick up or drop off passengers.
 */
@ApiModel
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stops")
public class Stop implements Serializable {

  private static final long serialVersionUID = 5145447936859483204L;

  /**
   * stop_id Required The stop_id field contains an ID that uniquely identifies a stop or station.
   * Multiple routes may use the same stop. The stop_id is dataset unique.
   */
  @Id
  @Column(name = "stop_id", nullable = false)
  private String stopId;

  /**
   * Back reference
   */
  @JsonBackReference
  @OneToMany(mappedBy = "stop", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<StopTime> stopTimes;

  /**
   * stop_name Required The stop_name field contains the name of a stop or station. Please use a
   * name that people will understand in the local and tourist vernacular.
   */
  @Column(name = "stop_name", nullable = false)
  private String name;

  @Column(name = "stop_lat")
  private Double latitude;

  @Column(name = "stop_lon")
  private Double longitude;

  /**
   * stop_code Optional The stop_code field contains short text or a number that uniquely identifies
   * the stop for passengers. Stop codes are often used in phone-based transit information systems
   * or printed on stop signage to make it easier for riders to get a stop schedule or real-time
   * arrival information for a particular stop. The stop_code field should only be used for stop
   * codes that are displayed to passengers. For internal codes, use stop_id. This field should be
   * left blank for stops without a code.
   */
  @Column(name = "stop_code")
  private String code;

  @Column(name = "platform_code")
  private String platformCode;

  /**
   * stop_desc Optional The stop_desc field contains a description of a stop. Please provide useful,
   * quality information. Do not simply duplicate the name of the stop.
   */
  @Column(name = "stop_desc")
  private String description;

  /**
   * zone_id Optional The zone_id field defines the fare zone for a stop ID. Zone IDs are required
   * if you want to provide fare information using fare_rules.txt. If this stop ID represents a
   * station, the zone ID is ignored.
   */
  @Column(name = "zone_id")
  private String zone;

  /**
   * stop_url Optional The stop_url field contains the URL of a web page about a particular stop.
   * This should be different from the agency_url and the route_url fields. The value must be a
   * fully qualified URL that includes http:// or https://, and any special characters in the URL
   * must be correctly escaped. See http://www.w3.org/Addressing/URL/4_URI_Recommentations.html for
   * a description of how to create fully qualified URL values.
   */
  @Column(name = "stop_url")
  private String url;

  /**
   * stop_timezone Optional The stop_timezone field contains the timezone in which this stop or
   * station is located. Please refer to Wikipedia List of Timezones for a list of valid values. If
   * omitted, the stop should be assumed to be located in the timezone specified by agency_timezone
   * in agency.txt.<br>
   * <br>
   * When a stop has a parent station, the stop is considered to be in the timezone specified by the
   * parent station's stop_timezone value. If the parent has no stop_timezone value, the stops that
   * belong to that station are assumed to be in the timezone specified by agency_timezone, even if
   * the stops have their own stop_timezone values. In other words, if a given stop has a
   * parent_station value, any stop_timezone value specified for that stop must be ignored.<br>
   * <br>
   * Even if stop_timezone values are provided in stops.txt, the times in stop_times.txt should
   * continue to be specified as time since midnight in the timezone specified by agency_timezone in
   * agency.txt. This ensures that the time values in a trip always increase over the course of a
   * trip, regardless of which timezones the trip crosses.
   */
  @Column(name = "stop_timezone")
  private String timezone;

  /**
   * parent_station Optional For stops that are physically located inside stations, the
   * parent_station field identifies the station associated with the stop. To use this field,
   * stops.txt must also contain a row where this stop ID is assigned location type=1.<br>
   *
   * <pre>
   * This stop ID represents...       This entry's location type...   This entry's parent_station field contains...
   * A stop located inside a station.     0 or blank          The stop ID of the station where this stop is located. The stop referenced by parent_station must have location_type=1.
   * A stop located outside a station.    0 or blank          A blank value. The parent_station field doesn't apply to this stop.
   * A station.               1               A blank value. Stations can't contain other stations.
   * </pre>
   */
  @Column(name = "parent_station")
  private String parentStation;

  /**
   * wheelchair_boarding Optional The wheelchair_boarding field identifies whether wheelchair
   * boardings are possible from the specified stop or station. The field can have the following
   * values:
   *
   * <pre>
   *     0 (or empty) - indicates that there is no accessibility information for the stop
   *     1 - indicates that at least some vehicles at this stop can be boarded by a rider in a wheelchair
   *     2 - wheelchair boarding is not possible at this stop
   * </pre>
   *
   * When a stop is part of a larger station complex, as indicated by a stop with a parent_station
   * value, the stop's wheelchair_boarding field has the following additional semantics:<br>
   *
   * <pre>
   *     0 (or empty) - the stop will inherit its wheelchair_boarding value from the parent station, if specified in the parent
   *     1 - there exists some accessible path from outside the station to the specific stop / platform
   *     2 - there exists no accessible path from outside the station to the specific stop / platform
   * </pre>
   *
   * @see WheelchairType refer to documentation for more details.
   */
  // @Column(name = "wheelchair_type")
  // private WheelchairType wheelchairType;

  /**
   * location_type Optional The location_type field identifies whether this stop ID represents a
   * stop or station. If no location type is specified, or the location_type is blank, stop IDs are
   * treated as stops. Stations may have different properties from stops when they are represented
   * on a map or used in trip planning.
   *
   * @see StopLocationType refer to documentation for more details.
   */
  @Column(name = "location_type")
  private StopLocationType locationType;

}
