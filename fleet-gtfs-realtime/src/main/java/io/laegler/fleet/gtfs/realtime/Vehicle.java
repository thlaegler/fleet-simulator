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
public class Vehicle implements Serializable {

  private static final long serialVersionUID = 5968891062820308924L;

  private static final ResponseField[] $responseFields =
      {ResponseField.forString("occupancyStatus", "occupancyStatus", null, true, emptyList()),
          ResponseField.forObject("trip", "trip", null, true, emptyList()),
          ResponseField.forString("stopId", "stopId", null, true, emptyList()),
          ResponseField.forDouble("timestamp", "timestamp", null, true, emptyList()),
          ResponseField.forDouble("currentStopSequence", "currentStopSequence", null, true,
              emptyList()),
          ResponseField.forString("congestionLevel", "congestionLevel", null, true, emptyList()),
          ResponseField.forObject("vehicle", "vehicle", null, true, emptyList()),
          ResponseField.forObject("position", "position", null, true, emptyList())};

  private String label;

  @ApiModelProperty("Additional information on the vehicle that is serving this trip. Each entry should have a unique vehicle id.")
  private VehicleDetail vehicle;

  @ApiModelProperty("Current position of this vehicle.")
  private Position position;

  @ApiModelProperty("Float Moment at which the vehicle's position was measured. In POSIX time (i.e., number of seconds since January 1st 1970 00:00:00 UTC).")
  private Long timestamp;

  @ApiModelProperty("The Trip that this vehicle is serving. Can be empty or partial if the vehicle can not be identified with a given trip instance.")
  private Trip trip;

  @ApiModelProperty("The stop sequence index of the current stop. The meaning of current_stop_sequence (i.e., the stop that it refers to) is determined by current_status. If current_status is missing IN_TRANSIT_TO is assumed.")
  private Double currentStopSequence;

  @ApiModelProperty("Identifies the current stop. The value must be the same as in stops.txt in the corresponding GTFS feed.")
  private String stopId;

  @ApiModelProperty("String The exact status of the vehicle with respect to the current stop.Ignored if current_stop_sequence is missing.")
  private String currentStatus;

  @ApiModelProperty("No Documentation")
  private CongestionLevel congestionLevel;

  @ApiModelProperty("The degree of passenger occupancy of the vehicle. Caution: this field is still experimental, and subject to change. It may be formally adopted in the future.")
  private OccupancyStatus occupancyStatus;

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  /**
   * The degree of passenger occupancy of the vehicle. Caution: this field is still experimental,
   * and subject to change. It may be formally adopted in the future.
   */
  public OccupancyStatus occupancyStatus() {
    return this.occupancyStatus;
  }

  /**
   * The Trip that this vehicle is serving. Can be empty or partial if the vehicle can not be
   * identified with a given trip instance.
   */
  public Trip trip() {
    return this.trip;
  }

  /**
   * Identifies the current stop. The value must be the same as in stops.txt in the corresponding
   * GTFS feed.
   */
  public String stopId() {
    return this.stopId;
  }

  /**
   * Moment at which the vehicle's position was measured. In POSIX time (i.e., number of seconds
   * since January 1st 1970 00:00:00 UTC).
   */
  public Long timestamp() {
    return this.timestamp;
  }

  /**
   * The stop sequence index of the current stop. The meaning of current_stop_sequence (i.e., the
   * stop that it refers to) is determined by current_status. If current_status is missing
   * IN_TRANSIT_TO is assumed.
   */
  public Double currentStopSequence() {
    return this.currentStopSequence;
  }

  public CongestionLevel congestionLevel() {
    return this.congestionLevel;
  }

  /**
   * Additional information on the vehicle that is serving this trip. Each entry should have a
   * unique vehicle id.
   */
  public VehicleDetail vehicle() {
    return this.vehicle;
  }

  /**
   * Current position of this vehicle.
   */
  public Position position() {
    return this.position;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeString($responseFields[0], occupancyStatus.name());
        writer.writeObject($responseFields[1], trip.marshaller());
        writer.writeString($responseFields[2], stopId);
        writer.writeLong($responseFields[3], timestamp);
        writer.writeDouble($responseFields[4], currentStopSequence);
        writer.writeString($responseFields[5], congestionLevel.name());
        writer.writeObject($responseFields[6], vehicle.marshaller());
        writer.writeObject($responseFields[7], position.marshaller());
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "Vehicle{" + "occupancyStatus=" + occupancyStatus + ", " + "trip=" + trip + ", "
          + "stopId=" + stopId + ", " + "timestamp=" + timestamp + ", " + "currentStopSequence="
          + currentStopSequence + ", " + "congestionLevel=" + congestionLevel + ", " + "vehicle="
          + vehicle + ", " + "position=" + position + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Vehicle) {
      Vehicle that = (Vehicle) o;
      return this.occupancyStatus.equals(that.occupancyStatus) && this.trip.equals(that.trip)
          && this.stopId.equals(that.stopId) && this.timestamp.equals(that.timestamp)
          && this.currentStopSequence.equals(that.currentStopSequence)
          && this.congestionLevel.equals(that.congestionLevel) && this.vehicle.equals(that.vehicle)
          && this.position.equals(that.position);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= occupancyStatus.hashCode();
      h *= 1000003;
      h ^= trip.hashCode();
      h *= 1000003;
      h ^= stopId.hashCode();
      h *= 1000003;
      h ^= timestamp.hashCode();
      h *= 1000003;
      h ^= currentStopSequence.hashCode();
      h *= 1000003;
      h ^= congestionLevel.hashCode();
      h *= 1000003;
      h ^= vehicle.hashCode();
      h *= 1000003;
      h ^= position.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<Vehicle> {
    final Trip.Mapper tripFieldMapper = new Trip.Mapper();

    final VehicleDetail.Mapper vehicleDetailFieldMapper = new VehicleDetail.Mapper();

    final Position.Mapper positionFieldMapper = new Position.Mapper();

    @Override
    public Vehicle map(ResponseReader reader) {
      final String occupancyStatusStr = reader.readString($responseFields[0]);
      final OccupancyStatus occupancyStatus;
      if (occupancyStatusStr != null) {
        occupancyStatus = OccupancyStatus.valueOf(occupancyStatusStr);
      } else {
        occupancyStatus = null;
      }
      final Trip trip =
          reader.readObject($responseFields[1], new ResponseReader.ObjectReader<Trip>() {
            @Override
            public Trip read(ResponseReader reader) {
              return tripFieldMapper.map(reader);
            }
          });
      final String stopId = reader.readString($responseFields[2]);
      final Long timestamp = reader.readLong($responseFields[3]);
      final Double currentStopSequence = reader.readDouble($responseFields[4]);
      final String congestionLevelStr = reader.readString($responseFields[5]);
      final CongestionLevel congestionLevel;
      if (congestionLevelStr != null) {
        congestionLevel = CongestionLevel.valueOf(congestionLevelStr);
      } else {
        congestionLevel = null;
      }
      final VehicleDetail vehicle =
          reader.readObject($responseFields[6], new ResponseReader.ObjectReader<VehicleDetail>() {
            @Override
            public VehicleDetail read(ResponseReader reader) {
              return vehicleDetailFieldMapper.map(reader);
            }
          });
      final Position position =
          reader.readObject($responseFields[7], new ResponseReader.ObjectReader<Position>() {
            @Override
            public Position read(ResponseReader reader) {
              return positionFieldMapper.map(reader);
            }
          });
      return Vehicle.builder().occupancyStatus(occupancyStatus).congestionLevel(congestionLevel)
          .vehicle(vehicle).position(position).currentStopSequence(currentStopSequence).trip(trip)
          .stopId(stopId).timestamp(timestamp).build();
    }
  }
}
