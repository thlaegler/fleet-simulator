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
import java.util.Currency;
import java.util.List;
import java.util.Map;
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
@Document(indexName = "#{fleetSimulatorProperties.elasticsearch.journey.get('indexName')}",
    type = "#{fleetSimulatorProperties.elasticsearch.journey.get('type')}")
@Setting
public class Journey implements Serializable {

  private static final long serialVersionUID = -6349300534003544633L;

  @ApiModelProperty(example = "BOOKING1234", readOnly = true, accessMode = READ_ONLY)
  @Id
  @Field(type = Keyword)
  @Builder.Default
  @NotNull
  private String bookingId = UUID.randomUUID().toString();

  @ApiModelProperty(example = "true", readOnly = true, accessMode = READ_ONLY)
  @Field(type = Text)
  private boolean available;

  @ApiModelProperty(example = "true", readOnly = true, accessMode = READ_ONLY)
  @Field(type = Text)
  private boolean active;

  @ApiModelProperty(example = "123243453453", value = "Timestamp of the last change/creation",
      readOnly = true, accessMode = READ_ONLY)
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

  @ApiModelProperty(example = "20% Discount Booking", readOnly = true, accessMode = READ_ONLY)
  @Field(type = Text)
  private String name;

  @ApiModelProperty(example = "this and that", readOnly = true, accessMode = READ_ONLY)
  @Field(type = Text)
  private String description;

  @ApiModelProperty
  @Field(type = Nested)
  private Passenger passenger;

  @ApiModelProperty(value = "Estimated Fare of Journey", example = "17.95", required = false,
      readOnly = true, accessMode = READ_ONLY)
  @Field(type = Double)
  private BigDecimal fare;

  @ApiModelProperty(value = "ISO 4217 Currency Code", example = "ABC123", required = false)
  @Field(type = Keyword)
  @Builder.Default
  private String currency = Currency.getInstance("NZD").getCurrencyCode();

  @ApiModelProperty(example = "BOOKED", required = false,
      allowableValues = "BOOKED,ENROUTE,PICKEDUP,ARRIVED,CANCELLED,INVALID,AVAILABLE",
      readOnly = true, accessMode = READ_ONLY)
  @Field(type = Keyword)
  @JsonDeserialize(using = StatusDeserializer.class)
  private Status status;

  @ApiModelProperty(required = false)
  @Field(type = Text)
  private List<TransportType> transportType;

  @ApiModelProperty(value = "Start time of the Journey", example = "2000-10-31T01:30:00.000",
      required = false)
  @Field(type = Date, format = DateFormat.date_hour_minute_second_millis)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Builder.Default
  private LocalDateTime start = LocalDateTime.now();

  @ApiModelProperty(value = "Estimated Time of Arrival in ISO DateTime Format",
      example = "2000-10-31T01:30:00.000", required = false, readOnly = true,
      accessMode = READ_ONLY)
  @Field(type = Date, format = DateFormat.date_hour_minute_second_millis)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime eta;

  @ApiModelProperty(example = "000ce023-0e3c-47ea-b148-7e300783b5b3", readOnly = true,
      accessMode = READ_ONLY)
  @Field(type = Keyword)
  private String providerId;

  @ApiModelProperty(accessMode = READ_ONLY, hidden = true)
  @Transient
  // @JsonIgnore
  private Provider provider;

  @ApiModelProperty(
      example = "[ '000ce023-0e3c-47ea-b148-7e300783b5b3', '000ce023-0e3c-47ea-b148-7e300783b5b3' ]")
  @Field(type = Keyword)
  private List<String> tripIds;

  @ApiModelProperty(readOnly = true, accessMode = READ_ONLY, hidden = true)
  @Transient
  @JsonIgnore
  private Map<String, Trip> trips;

  @ApiModelProperty
  @Field(type = Nested)
  private Position position;

  @ApiModelProperty
  @Field(type = Keyword)
  private String vehicleId;

  @ApiModelProperty
  @Transient
  private Vehicle vehicle;

  @Field(type = Keyword)
  private String gtfsRouteId;

  @Field(type = Keyword)
  private String gtfsTripId;

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[id=" + getBookingId() + "]";
  }

}
