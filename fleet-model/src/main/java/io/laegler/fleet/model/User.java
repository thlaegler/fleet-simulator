package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static io.swagger.annotations.ApiModelProperty.AccessMode.READ_ONLY;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class User implements Serializable {

  private static final long serialVersionUID = -6349300534003544633L;

  @ApiModelProperty(example = "0c0d518cb3828c7955c2dedb366cbb3f", readOnly = true,
      accessMode = READ_ONLY)
  @Id
  @NotNull
  private String userToken;

  @ApiModelProperty
  @Id
  @Builder.Default
  private Map<String, Journey> journeys = new HashMap<>();

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[id=" + getUserToken() + "]";
  }

}
