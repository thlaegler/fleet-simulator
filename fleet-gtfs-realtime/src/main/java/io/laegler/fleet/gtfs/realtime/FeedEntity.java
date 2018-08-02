package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.util.Collections.emptyList;
import java.io.Serializable;
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

@ApiModel
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class FeedEntity implements Serializable {

  private static final long serialVersionUID = 5961883867230057962L;

  private static final ResponseField[] $responseFields =
      {ResponseField.forString("isDeleted", "isDeleted", null, true, emptyList()),
          ResponseField.forObject("tripUpdate", "tripUpdate", null, true, emptyList()),
          ResponseField.forObject("alert", "alert", null, true, emptyList()),
          ResponseField.forObject("vehicle", "vehicle", null, true, emptyList())};

  @ApiModelProperty("Feed-unique identifier for this entity. The ids are used only to provide incrementality support. The actual entities referenced by the feed must be specified by explicit selectors (see EntitySelector below for more info).")
  private String id;

  @ApiModelProperty(name = "vehicle",
      value = "Data about the realtime position of a vehicle. At least one of the fields trip_update, vehicle, or alert must be provided - all these fields cannot be empty.")
  private Vehicle vehicle;

  @ApiModelProperty("Whether this entity is to be deleted. Should be provided only for feeds with Incrementality of DIFFERENTIAL - this field should NOT be provided for feeds with Incrementality of FULL_DATASET.")
  private String isDeleted;

  @ApiModelProperty("Data about the realtime departure delays of a trip. At least one of the fields trip_update, vehicle, or alert must be provided - all these fields cannot be empty.")
  private TripUpdate tripUpdate;

  @ApiModelProperty("Data about the realtime alert. At least one of the fields trip_update, vehicle, or alert must be provided - all these fields cannot be empty.")
  private Alert alert;

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  /**
   * Whether this entity is to be deleted. Should be provided only for feeds with Incrementality of
   * DIFFERENTIAL - this field should NOT be provided for feeds with Incrementality of FULL_DATASET.
   */
  public String isDeleted() {
    return this.isDeleted;
  }

  /**
   * Data about the realtime departure delays of a trip. At least one of the fields trip_update,
   * vehicle, or alert must be provided - all these fields cannot be empty.
   */
  public TripUpdate tripUpdate() {
    return this.tripUpdate;
  }

  /**
   * Data about the realtime alert. At least one of the fields trip_update, vehicle, or alert must
   * be provided - all these fields cannot be empty.
   */
  public Alert alert() {
    return this.alert;
  }

  /**
   * Data about the realtime position of a vehicle. At least one of the fields trip_update, vehicle,
   * or alert must be provided - all these fields cannot be empty.
   */
  public Vehicle vehicle() {
    return this.vehicle;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeString($responseFields[0], isDeleted);
        writer.writeObject($responseFields[1], tripUpdate.marshaller());
        writer.writeObject($responseFields[2], alert.marshaller());
        writer.writeObject($responseFields[3], vehicle.marshaller());
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "Feed{" + "isDeleted=" + isDeleted + ", " + "tripUpdate=" + tripUpdate + ", "
          + "alert=" + alert + ", " + "vehicle=" + vehicle + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof FeedEntity) {
      FeedEntity that = (FeedEntity) o;
      return this.isDeleted.equals(that.isDeleted) && this.tripUpdate.equals(that.tripUpdate)
          && this.alert.equals(that.alert) && this.vehicle.equals(that.vehicle);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= isDeleted.hashCode();
      h *= 1000003;
      h ^= tripUpdate.hashCode();
      h *= 1000003;
      h ^= alert.hashCode();
      h *= 1000003;
      h ^= vehicle.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<FeedEntity> {
    final TripUpdate.Mapper tripUpdateFieldMapper = new TripUpdate.Mapper();

    final Alert.Mapper alertFieldMapper = new Alert.Mapper();

    final Vehicle.Mapper vehicleFieldMapper = new Vehicle.Mapper();

    @Override
    public FeedEntity map(ResponseReader reader) {
      final String isDeleted = reader.readString($responseFields[0]);
      final TripUpdate tripUpdate =
          reader.readObject($responseFields[1], new ResponseReader.ObjectReader<TripUpdate>() {
            @Override
            public TripUpdate read(ResponseReader reader) {
              return tripUpdateFieldMapper.map(reader);
            }
          });
      final Alert alert =
          reader.readObject($responseFields[2], new ResponseReader.ObjectReader<Alert>() {
            @Override
            public Alert read(ResponseReader reader) {
              return alertFieldMapper.map(reader);
            }
          });
      final Vehicle vehicle =
          reader.readObject($responseFields[3], new ResponseReader.ObjectReader<Vehicle>() {
            @Override
            public Vehicle read(ResponseReader reader) {
              return vehicleFieldMapper.map(reader);
            }
          });
      return FeedEntity.builder().isDeleted(isDeleted).tripUpdate(tripUpdate).alert(alert)
          .vehicle(vehicle).build();
    }
  }

}
