package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static io.swagger.annotations.ApiModelProperty.AccessMode.READ_ONLY;
import java.io.Serializable;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.maps.model.Bounds;
import com.google.maps.model.Fare;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
// @Entity
public class Route implements Serializable {

  private static final long serialVersionUID = 4943435036281622142L;

  @ApiModelProperty
  private String summary;

  @ApiModelProperty
  private List<Leg> legs;

  @ApiModelProperty
  private List<Integer> waypoint_order;

  @ApiModelProperty
  private EncodedPolyline overview_polyline;

  @ApiModelProperty
  private Bounds bounds;

  @ApiModelProperty
  private String copyrights;

  @ApiModelProperty
  private Fare fare;

  @ApiModelProperty
  private List<String> warnings;

  @ApiModelProperty
  // @JsonSerialize(using = LocalDateTimeSerializer.class)
  // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private long startTimestampUtc;

  @ApiModelProperty(readOnly = true, accessMode = READ_ONLY)
  // @JsonSerialize(using = LocalDateTimeSerializer.class)
  // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private long etaTimestampUtc;

  @ApiModelProperty
  @JsonProperty("active")
  private boolean isActive;

}
