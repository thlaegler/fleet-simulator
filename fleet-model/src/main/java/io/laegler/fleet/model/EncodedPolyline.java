package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import java.io.Serializable;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Validated
// @Entity
public class EncodedPolyline implements Serializable {

  private static final long serialVersionUID = -588745973779570603L;

  @ApiModelProperty(
      example = "bao_F}muv@y@bAg@nnAzAp@Tn@ExBw@pCDhB?TAj@IxAGvAOdCGdCTtAv@t@^AbAZb@?d@E")
  private String points;

  public List<LatLng> decodePath() {
    return PolylineEncoding.decode(points);
  }

}
