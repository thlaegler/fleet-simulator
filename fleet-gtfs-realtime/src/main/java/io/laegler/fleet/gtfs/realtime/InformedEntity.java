package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.util.Collections.emptyList;
import java.io.Serializable;
import javax.annotation.Nullable;
import org.springframework.validation.annotation.Validated;
import com.apollographql.apollo.api.ResponseField;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseFieldMarshaller;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.ResponseWriter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(
    description = "A selector for an entity in a GTFS feed. The values of the fields should correspond to the appropriate fields in the GTFS feed. At least one specifier must be given. If several are given, then the matching has to apply to all the given specifiers.")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class InformedEntity implements Serializable {

  private static final long serialVersionUID = 7647363314088400546L;

  static final ResponseField[] $responseFields =
      {ResponseField.forString("agencyId", "agencyId", null, true, emptyList()),
          ResponseField.forString("routeId", "routeId", null, true, emptyList()),
          ResponseField.forString("stopId", "stopId", null, true, emptyList()),
          ResponseField.forObject("trip", "trip", null, true, emptyList()),
          ResponseField.forDouble("routeType", "routeType", null, true, emptyList())};

  @ApiModelProperty("At least one specifier must be given - all fields in an EntitySelector cannot be empty.")
  private String agencyId;

  @ApiModelProperty("At least one specifier must be given - all fields in an EntitySelector cannot be empty.")
  private String routeId;

  @ApiModelProperty("At least one specifier must be given - all fields in an EntitySelector cannot be empty.")
  private Double routeType;

  @ApiModelProperty("At least one specifier must be given - all fields in an EntitySelector cannot be empty.")
  private Trip trip;

  @ApiModelProperty("At least one specifier must be given - all fields in an EntitySelector cannot be empty.")
  private String stopId;


  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  public InformedEntity(@Nullable String agencyId, @Nullable String routeId,
      @Nullable String stopId, @Nullable Trip trip, @Nullable Double routeType) {
    this.agencyId = agencyId;
    this.routeId = routeId;
    this.stopId = stopId;
    this.trip = trip;
    this.routeType = routeType;
  }

  /**
   * At least one specifier must be given - all fields in an EntitySelector cannot be empty.
   */
  public String agencyId() {
    return this.agencyId;
  }

  /**
   * At least one specifier must be given - all fields in an EntitySelector cannot be empty.
   */
  public String routeId() {
    return this.routeId;
  }

  /**
   * At least one specifier must be given - all fields in an EntitySelector cannot be empty.
   */
  public String stopId() {
    return this.stopId;
  }

  /**
   * At least one specifier must be given - all fields in an EntitySelector cannot be empty.
   */
  public Trip trip() {
    return this.trip;
  }

  /**
   * At least one specifier must be given - all fields in an EntitySelector cannot be empty.
   */
  public Double routeType() {
    return this.routeType;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeString($responseFields[0], agencyId);
        writer.writeString($responseFields[1], routeId);
        writer.writeString($responseFields[2], stopId);
        writer.writeObject($responseFields[3], trip.marshaller());
        writer.writeDouble($responseFields[4], routeType);
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "InformedEntity{" + "agencyId=" + agencyId + ", " + "routeId=" + routeId + ", "
          + "stopId=" + stopId + ", " + "trip=" + trip + ", " + "routeType=" + routeType + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof InformedEntity) {
      InformedEntity that = (InformedEntity) o;
      return this.agencyId.equals(that.agencyId) && this.routeId.equals(that.routeId)
          && this.stopId.equals(that.stopId) && this.trip.equals(that.trip)
          && this.routeType.equals(that.routeType);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= agencyId.hashCode();
      h *= 1000003;
      h ^= routeId.hashCode();
      h *= 1000003;
      h ^= stopId.hashCode();
      h *= 1000003;
      h ^= trip.hashCode();
      h *= 1000003;
      h ^= routeType.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<InformedEntity> {
    final Trip.Mapper trip1FieldMapper = new Trip.Mapper();

    @Override
    public InformedEntity map(ResponseReader reader) {
      final String agencyId = reader.readString($responseFields[0]);
      final String routeId = reader.readString($responseFields[1]);
      final String stopId = reader.readString($responseFields[2]);
      final Trip trip =
          reader.readObject($responseFields[3], new ResponseReader.ObjectReader<Trip>() {
            @Override
            public Trip read(ResponseReader reader) {
              return trip1FieldMapper.map(reader);
            }
          });
      final Double routeType = reader.readDouble($responseFields[4]);
      return new InformedEntity(agencyId, routeId, stopId, trip, routeType);
    }
  }
}
