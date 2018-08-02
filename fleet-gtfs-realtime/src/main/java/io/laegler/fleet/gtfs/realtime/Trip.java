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

@ApiModel(
    description = "A descriptor that identifies an instance of a GTFS trip, or all instances of a trip along a route. To specify a single trip instance, the trip_id (and if necessary, start_time) is set. If route_id is also set, then it should be same as one that the given trip corresponds to. To specify all the trips along a given route, only the route_id should be set. Note that if the trip_id is not known, then station sequence ids in TripUpdate are not sufficient, and stop_ids must be provided as well. In addition, absolute arrival/departure times must be provided.")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Trip implements Serializable {

  private static final long serialVersionUID = 85324632824643814L;

  static final ResponseField[] $responseFields = {
      ResponseField.forString("tripId", "tripId", null, true, emptyList()),
      ResponseField.forString("routeId", "routeId", null, true, emptyList()),
      ResponseField.forString("startTime", "startTime", null, true, emptyList()),
      ResponseField.forString("startDate", "startDate", null, true, emptyList()),
      ResponseField.forDouble("directionId", "directionId", null, true, emptyList()), ResponseField
          .forString("scheduleRelationship", "scheduleRelationship", null, true, emptyList())};

  @ApiModelProperty("The trip_id from the GTFS feed that this selector refers to. For non frequency-based trips (trips not defined in GTFS frequencies.txt), this field is enough to uniquely identify the trip. For frequency-based trips defined in GTFS frequencies.txt, trip_id, start_time, and start_date are all required. For scheduled-based trips (trips not defined in GTFS frequencies.txt), trip_id can only be omitted if the trip can be uniquely identified by a combination of route_id, direction_id, start_time, and start_date, and all those fields are provided.")
  private String tripId;

  @ApiModelProperty("The route_id from the GTFS that this selector refers to. If trip_id is omitted, route_id must be provided.")
  private String routeId;

  @ApiModelProperty("The direction_id from the GTFS feed trips.txt file, indicating the direction of travel for trips this selector refers to. If trip_id is omitted, direction_id must be provided. Caution: this field is still experimental, and subject to change. It may be formally adopted in the future.")
  private Double directionId;

  @ApiModelProperty("The initially scheduled start time of this trip instance. When the trip_id corresponds to a non-frequency-based trip, this field should either be omitted or be equal to the value in the GTFS feed. When the trip_id correponds to a frequency-based trip defined in GTFS frequencies.txt, start_time is required and must be specified for trip updates and vehicle positions. If the trip corresponds to exact_times=1 GTFS record, then start_time must be some multiple (including zero) of headway_secs later than frequencies.txt start_time for the corresponding time period. If the trip corresponds to exact_times=0, then its start_time may be arbitrary, and is initially expected to be the first departure of the trip. Once established, the start_time of this frequency-based exact_times=0 trip should be considered immutable, even if the first departure time changes -- that time change may instead be reflected in a StopTimeUpdate. If trip_id is omitted, start_time must be provided. Format and semantics of the field is same as that of GTFS/frequencies.txt/start_time, e.g., 11:15:35 or 25:15:35.")
  private String startTime;

  @ApiModelProperty("The start date of this trip instance in YYYYMMDD format. For scheduled trips (trips not defined in GTFS frequencies.txt), this field must be provided to disambiguate trips that are so late as to collide with a scheduled trip on a next day. For example, for a train that departs 8:00 and 20:00 every day, and is 12 hours late, there would be two distinct trips on the same time. This field can be provided but is not mandatory for schedules in which such collisions are impossible - for example, a service running on hourly schedule where a vehicle that is one hour late is not considered to be related to schedule anymore. This field is required for frequency-based trips defined in GTFS frequencies.txt. If trip_id is omitted, start_date must be provided.")
  private String startDate;

  @ApiModelProperty("No Documentation")
  private TripScheduleRelationship scheduleRelationship;


  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  /**
   * The trip_id from the GTFS feed that this selector refers to. For non frequency-based trips
   * (trips not defined in GTFS frequencies.txt), this field is enough to uniquely identify the
   * trip. For frequency-based trips defined in GTFS frequencies.txt, trip_id, start_time, and
   * start_date are all required. For scheduled-based trips (trips not defined in GTFS
   * frequencies.txt), trip_id can only be omitted if the trip can be uniquely identified by a
   * combination of route_id, direction_id, start_time, and start_date, and all those fields are
   * provided.
   */
  public String tripId() {
    return this.tripId;
  }

  /**
   * The route_id from the GTFS that this selector refers to. If trip_id is omitted, route_id must
   * be provided.
   */
  public String routeId() {
    return this.routeId;
  }

  /**
   * The initially scheduled start time of this trip instance. When the trip_id corresponds to a
   * non-frequency-based trip, this field should either be omitted or be equal to the value in the
   * GTFS feed. When the trip_id correponds to a frequency-based trip defined in GTFS
   * frequencies.txt, start_time is required and must be specified for trip updates and vehicle
   * positions. If the trip corresponds to exact_times=1 GTFS record, then start_time must be some
   * multiple (including zero) of headway_secs later than frequencies.txt start_time for the
   * corresponding time period. If the trip corresponds to exact_times=0, then its start_time may be
   * arbitrary, and is initially expected to be the first departure of the trip. Once established,
   * the start_time of this frequency-based exact_times=0 trip should be considered immutable, even
   * if the first departure time changes -- that time change may instead be reflected in a
   * StopTimeUpdate. If trip_id is omitted, start_time must be provided. Format and semantics of the
   * field is same as that of GTFS/frequencies.txt/start_time, e.g., 11:15:35 or 25:15:35.
   */
  public String startTime() {
    return this.startTime;
  }

  /**
   * The start date of this trip instance in YYYYMMDD format. For scheduled trips (trips not defined
   * in GTFS frequencies.txt), this field must be provided to disambiguate trips that are so late as
   * to collide with a scheduled trip on a next day. For example, for a train that departs 8:00 and
   * 20:00 every day, and is 12 hours late, there would be two distinct trips on the same time. This
   * field can be provided but is not mandatory for schedules in which such collisions are
   * impossible - for example, a service running on hourly schedule where a vehicle that is one hour
   * late is not considered to be related to schedule anymore. This field is required for
   * frequency-based trips defined in GTFS frequencies.txt. If trip_id is omitted, start_date must
   * be provided.
   */
  public String startDate() {
    return this.startDate;
  }

  /**
   * The direction_id from the GTFS feed trips.txt file, indicating the direction of travel for
   * trips this selector refers to. If trip_id is omitted, direction_id must be provided. Caution:
   * this field is still experimental, and subject to change. It may be formally adopted in the
   * future.
   */
  public Double directionId() {
    return this.directionId;
  }

  public TripScheduleRelationship scheduleRelationship() {
    return this.scheduleRelationship;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeString($responseFields[0], tripId);
        writer.writeString($responseFields[1], routeId);
        writer.writeString($responseFields[2], startTime);
        writer.writeString($responseFields[3], startDate);
        writer.writeDouble($responseFields[4], directionId);
        writer.writeString($responseFields[5], scheduleRelationship.name());
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "Trip2{" + "tripId=" + tripId + ", " + "routeId=" + routeId + ", " + "startTime="
          + startTime + ", " + "startDate=" + startDate + ", " + "directionId=" + directionId + ", "
          + "scheduleRelationship=" + scheduleRelationship + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Trip) {
      Trip that = (Trip) o;
      return this.tripId.equals(that.tripId) && this.routeId.equals(that.routeId)
          && this.startTime.equals(that.startTime) && this.startDate.equals(that.startDate)
          && this.directionId.equals(that.directionId)
          && this.scheduleRelationship.equals(that.scheduleRelationship);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= tripId.hashCode();
      h *= 1000003;
      h ^= routeId.hashCode();
      h *= 1000003;
      h ^= startTime.hashCode();
      h *= 1000003;
      h ^= startDate.hashCode();
      h *= 1000003;
      h ^= directionId.hashCode();
      h *= 1000003;
      h ^= scheduleRelationship.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<Trip> {
    @Override
    public Trip map(ResponseReader reader) {
      final String tripId = reader.readString($responseFields[0]);
      final String routeId = reader.readString($responseFields[1]);
      final String startTime = reader.readString($responseFields[2]);
      final String startDate = reader.readString($responseFields[3]);
      final Double directionId = reader.readDouble($responseFields[4]);
      final String scheduleRelationshipStr = reader.readString($responseFields[5]);
      final TripScheduleRelationship scheduleRelationship;
      if (scheduleRelationshipStr != null) {
        scheduleRelationship = TripScheduleRelationship.valueOf(scheduleRelationshipStr);
      } else {
        scheduleRelationship = null;
      }
      return Trip.builder().tripId(tripId).routeId(routeId).startDate(startDate)
          .startTime(startTime).directionId(directionId).scheduleRelationship(scheduleRelationship)
          .build();
    }
  }

}
