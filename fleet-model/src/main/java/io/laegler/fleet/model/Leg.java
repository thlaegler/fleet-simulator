package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import io.laegler.fleet.json.LocalDateTimeDeserializer;
import io.laegler.fleet.json.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "This is the Client who books/booked a Journey")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Leg {

  /**
   * Contains an array of steps denoting information about each separate step of this leg of the
   * journey.
   */
  private List<Step> steps;

  /** The total distance covered by this leg. */
  private Distance distance;

  /** The total duration of this leg. */
  private Duration duration;

  /**
   * The total duration of this leg, taking into account current traffic conditions. The duration in
   * traffic will only be returned if all of the following are true:
   *
   * <ol>
   * <li>The directions request includes a departureTime parameter set to a value within a few
   * minutes of the current time.
   * <li>The request includes a valid Maps for Work client and signature parameter.
   * <li>Traffic conditions are available for the requested route.
   * <li>The directions request does not include stopover waypoints.
   * </ol>
   */
  private Duration durationInTraffic;

  /**
   * The estimated time of arrival for this leg. This property is only returned for transit
   * directions.
   */
  @ApiModelProperty(value = "Time of Arrival", example = "23:30:00", required = false)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Builder.Default
  private LocalDateTime arrivalTime = LocalDateTime.now();

  /**
   * The estimated time of departure for this leg. The departureTime is only available for transit
   * directions.
   */
  @ApiModelProperty(value = "Time of Departure", example = "23:30:00", required = false)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Builder.Default
  private LocalDateTime departureTime = LocalDateTime.now();

  /**
   * The latitude/longitude coordinates of the origin of this leg. Because the Directions API
   * calculates directions between locations by using the nearest transportation option (usually a
   * road) at the start and end points, startLocation may be different from the provided origin of
   * this leg if, for example, a road is not near the origin.
   */
  private LatLng startLocation;

  /**
   * The latitude/longitude coordinates of the given destination of this leg. Because the Directions
   * API calculates directions between locations by using the nearest transportation option (usually
   * a road) at the start and end points, endLocation may be different than the provided destination
   * of this leg if, for example, a road is not near the destination.
   */
  private LatLng endLocation;

  /**
   * The human-readable address (typically a street address) reflecting the start location of this
   * leg.
   */
  private String startAddress;

  /**
   * The human-readable address (typically a street address) reflecting the end location of this
   * leg.
   */
  private String endAddress;
}
