package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static io.swagger.annotations.ApiModelProperty.AccessMode.READ_ONLY;
import static org.springframework.data.elasticsearch.annotations.FieldType.Date;
import static org.springframework.data.elasticsearch.annotations.FieldType.Double;
import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.maps.model.DirectionsRoute;
import io.laegler.fleet.json.LocalDateTimeDeserializer;
import io.laegler.fleet.json.LocalDateTimeSerializer;
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
@Document(indexName = "#{fleetSimulatorProperties.elasticsearch.trip.get('indexName')}",
    type = "#{fleetSimulatorProperties.elasticsearch.trip.get('type')}")
@Setting
public class Trip implements Serializable {

  private static final long serialVersionUID = 1938774642856549142L;

  @ApiModelProperty(accessMode = READ_ONLY, example = "000ce023-0e3c-47ea-b148-7e300783b5b3")
  @Field(type = Keyword)
  @Id
  @Builder.Default
  @NotNull
  private String tripId = UUID.randomUUID().toString();

  @ApiModelProperty(example = "123243453453", value = "Timestamp of the last change/creation",
      accessMode = READ_ONLY)
  @Field(type = Date)
  private long timestampUTC;

  @ApiModelProperty
  @Field(type = Nested)
  @NotNull
  private Position from;

  @ApiModelProperty
  @Field(type = Nested)
  @NotNull
  private Position to;

  @ApiModelProperty
  @Field(type = Nested)
  private Position position;

  @ApiModelProperty(value = "Result of Google Directions/Route Calculation")
  @Field(type = Nested)
  private DirectionsRoute route;

  @ApiModelProperty
  @Field(type = Text)
  private String description;

  @ApiModelProperty(example = "PICKUP", required = false,
      allowableValues = "BOOKED,ENROUTE,PICKUP,PICKED_UP,ARRIVED,CANCELLED,INVALID,AVAILABLE,OUT_OF_SERVICE")
  @Field(type = Keyword)
  @NotNull
  @JsonDeserialize(using = StatusDeserializer.class)
  private Status status;

  @ApiModelProperty(example = "12.78")
  @Field(type = Double)
  private BigDecimal fare;

  @ApiModelProperty(example = "NZD")
  @Field(type = Keyword)
  @Builder.Default
  private String currency = "NZD";

  @ApiModelProperty(example = "000ce023-0e3c-47ea-b148-7e300783b5b3")
  @Field(type = Keyword)
  private String vehicleId;

  @ApiModelProperty
  @Transient
  @JsonIgnore
  private Vehicle vehicle;

  @ApiModelProperty(example = "000ce023-0e3c-47ea-b148-7e300783b5b3")
  @Field(type = Keyword)
  private String providerId;

  @ApiModelProperty
  @Transient
  @JsonIgnore
  private Provider provider;

  @ApiModelProperty
  @Field(type = Keyword)
  private TripType tripType;

  @ApiModelProperty
  @Field(type = Nested)
  private Passenger passenger;

  @ApiModelProperty(value = "Start of the trip ISO DateTime Format",
      example = "2000-10-31T01:30:00.000", required = false)
  @Field(type = Date, format = DateFormat.date_hour_minute_second_millis)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Builder.Default
  private LocalDateTime start = LocalDateTime.now();

  @ApiModelProperty(value = "Estimated Time of Arrival in ISO DateTime Format",
      example = "2000-10-31T01:30:00.000", required = false)
  @Field(type = Date, format = DateFormat.date_hour_minute_second_millis)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime eta;

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[id=" + getTripId() + "]";
  }

}
