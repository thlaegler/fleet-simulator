package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static io.swagger.annotations.ApiModelProperty.AccessMode.READ_ONLY;
import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
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
public class Driver implements Serializable {

  private static final long serialVersionUID = 1938774642856549142L;

  @ApiModelProperty(accessMode = READ_ONLY, example = "000ce023-0e3c-47ea-b148-7e300783b5b3")
  @Id
  @Field(type = Keyword)
  @Builder.Default
  @NotNull
  private String driverId = UUID.randomUUID().toString();

  @ApiModelProperty
  @Field(type = Text)
  private String name;

  @ApiModelProperty(value = "Phone number of the Driver", example = "4045965544")
  @Field(type = Text)
  private String phoneNumber;

  @ApiModelProperty
  @Field(type = Keyword)
  private String licenceId;

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[id=" + getDriverId() + "]";
  }

}
