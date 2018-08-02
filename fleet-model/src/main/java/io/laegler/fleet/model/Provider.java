package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static io.swagger.annotations.ApiModelProperty.AccessMode.READ_ONLY;
import static org.springframework.data.elasticsearch.annotations.FieldType.Boolean;
import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
// @Entity
@Document(indexName = "#{fleetSimulatorProperties.elasticsearch.provider.get('indexName')}",
    type = "#{fleetSimulatorProperties.elasticsearch.provider.get('type')}")
@Setting
public class Provider implements Serializable {

  private static final long serialVersionUID = -6349300534003544633L;

  @ApiModelProperty(accessMode = READ_ONLY, example = "000ce023-0e3c-47ea-b148-7e300783b5b3")
  @Id
  // @javax.persistence.Id
  @Field(type = Keyword)
  @Builder.Default
  @NotNull
  private String providerId = UUID.randomUUID().toString();

  @ApiModelProperty(example = "Corporate Cabs")
  @Field(type = Text)
  private String name;

  @ApiModelProperty(example = "Corporate Cabs")
  @Field(type = Text)
  private String description;

  @ApiModelProperty(example = "TAXI", allowableValues = "TAXI,BUS,SHUTTLE")
  @Field(type = Keyword)
  @NotNull
  private ProviderType type;

  @ApiModelProperty(example = "REST", allowableValues = "WEBSOCKET,REST,SOAP,GRPC")
  @Field(type = Keyword)
  @NotNull
  private ProviderProtocol protocol;

  @ApiModelProperty(example = "true")
  @Field(type = Boolean)
  @Builder.Default
  private boolean enabled = true;

  @ApiModelProperty(example = "mobilityos-satori-mock:8080")
  @Field(type = Text)
  private String baseurl;

  @ApiModelProperty
  @Field(type = Nested)
  private ProviderEndpoint availability;

  @ApiModelProperty
  @Field(type = Nested)
  private ProviderEndpoint booking;

  @ApiModelProperty
  @Field(type = Nested)
  private ProviderEndpoint bookingstatus;

  @ApiModelProperty
  @Field(type = Nested)
  private ProviderEndpoint cancelbooking;

  @ApiModelProperty(hidden = true)
  @Transient
  @JsonIgnore
  private Map<String, Vehicle> vehicles;

  @ApiModelProperty(value = "Shape of the geo fence of the operation area of this provider")
  @Field(type = Nested)
  private List<Position> operationArea;

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[id=" + getProviderId() + "]";
  }

}
