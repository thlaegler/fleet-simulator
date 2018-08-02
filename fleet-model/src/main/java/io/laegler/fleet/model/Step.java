package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import java.io.Serializable;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.maps.model.TransitDetails;
import com.google.maps.model.TravelMode;
import io.swagger.annotations.ApiModel;
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
public class Step implements Serializable {

  private static final long serialVersionUID = 4028343183153505311L;

  /** Formatted instructions for this step, presented as an HTML text string. */
  private String htmlInstructions;

  /** The distance covered by this step until the next step. */
  private Distance distance;

  /**
   * The maneuver required to move ahead. E.g., turn-left. Please note, this field is undocumented,
   * and thus should not be relied upon.
   */
  @Deprecated
  private String maneuver;

  /** The typical time required to perform the step, until the next step. */
  private Duration duration;

  /** The location of the starting point of this step. */
  private LatLng startLocation;

  /** The location of the last point of this step. */
  private LatLng endLocation;

  /**
   * Detailed directions for walking or driving steps in transit directions. Substeps are only
   * available when travelMode is set to "transit".
   */
  private List<Step> steps;

  /** The path of this step. */
  private EncodedPolyline polyline;

  /**
   * The travel mode of this step. See
   * <a href="https://developers.google.com/maps/documentation/directions/intro#TravelModes">Travel
   * Modes</a> for more detail.
   */
  private TravelMode travelMode;

  /**
   * Transit-specific information. This field is only returned when travel_mode is set to "transit".
   * See <a href=
   * "https://developers.google.com/maps/documentation/directions/intro#TransitDetails">Transit
   * Details</a> for more detail.
   */
  public TransitDetails transitDetails;

}
