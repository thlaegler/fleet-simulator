package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static io.swagger.annotations.ApiModelProperty.AccessMode.READ_ONLY;
import static org.springframework.data.elasticsearch.annotations.FieldType.Date;
import static org.springframework.data.elasticsearch.annotations.FieldType.Double;
import static org.springframework.data.elasticsearch.annotations.FieldType.Integer;
import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.laegler.fleet.json.StatusDeserializer;
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
@Document(indexName = "#{fleetSimulatorProperties.elasticsearch.vehicle.get('indexName')}",
    type = "#{fleetSimulatorProperties.elasticsearch.vehicle.get('type')}")
@Setting
public class Vehicle implements Serializable {

  private static final long serialVersionUID = 8086742759088291797L;

  @ApiModelProperty(example = "000ce023-0e3c-47ea-b148-7e300783b5b3", accessMode = READ_ONLY)
  @Id
  // @javax.persistence.Id
  @Field(type = Keyword)
  @Builder.Default
  @NotNull
  private String vehicleId = UUID.randomUUID().toString();

  @ApiModelProperty(example = "123243453453", value = "Timestamp of the last change/creation",
      readOnly = true, accessMode = READ_ONLY)
  @Field(type = Date)
  private Long timestampUTC;

  @ApiModelProperty(readOnly = true, accessMode = READ_ONLY)
  @Field(type = Keyword)
  @NotNull
  private String licencePlate;

  @ApiModelProperty(readOnly = true, accessMode = READ_ONLY)
  @Field(type = Text)
  private String description;

  @ApiModelProperty(readOnly = true, accessMode = READ_ONLY)
  @Field(type = Keyword)
  private String providerId;

  @ApiModelProperty
  @Field(type = Nested, ignoreFields = {"vehicles"})
  @Transient
  private Provider provider;

  @ApiModelProperty(readOnly = true, accessMode = READ_ONLY)
  @Field(type = Nested)
  @NotNull
  private Driver driver;

  @ApiModelProperty(example = "TAXI", readOnly = true, accessMode = READ_ONLY)
  @Field(type = Keyword)
  @NotNull
  private VehicleType vehicleType;

  @ApiModelProperty
  @Field(type = Double)
  private double speed;

  @ApiModelProperty
  @Field(type = Keyword)
  @NotNull
  @JsonDeserialize(using = StatusDeserializer.class)
  private Status status;

  @ApiModelProperty(readOnly = true, accessMode = READ_ONLY)
  @Field(type = Integer)
  private int maxPassengers;

  @ApiModelProperty
  @Field(type = Nested)
  @NotNull
  private Position position;

  @ApiModelProperty
  @Transient
  private Map<String, Trip> trips;

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[id=" + getVehicleId() + "]";
  }

}
