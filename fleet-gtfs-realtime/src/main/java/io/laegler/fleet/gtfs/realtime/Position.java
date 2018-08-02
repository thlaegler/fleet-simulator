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

@ApiModel(description = "Current Position of a Vehicle")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Position implements Serializable {

  private static final long serialVersionUID = -840579008571919310L;

  @ApiModelProperty(value = "Longitude")
  private double longitude;

  @ApiModelProperty(value = "Latitude")
  private double latitude;

  @ApiModelProperty(value = "Bearing")
  private Double bearing;

  @ApiModelProperty(value = "Odometer value, in meters.")
  private Double odometer;

  @ApiModelProperty(value = "Momentary speed measured by the vehicle, in meters per second.")
  private Double speed;

  static final ResponseField[] $responseFields =
      {ResponseField.forDouble("odometer", "odometer", null, true, emptyList()),
          ResponseField.forDouble("speed", "speed", null, true, emptyList()),
          ResponseField.forDouble("longitude", "longitude", null, false, emptyList()),
          ResponseField.forDouble("latitude", "latitude", null, false, emptyList()),
          ResponseField.forDouble("bearing", "bearing", null, true, emptyList())};

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  public Position(@Nullable Double odometer, @Nullable Double speed, double longitude,
      double latitude, @Nullable Double bearing) {
    this.odometer = odometer;
    this.speed = speed;
    this.longitude = longitude;
    this.latitude = latitude;
    this.bearing = bearing;
  }

  /**
   * Odometer value, in meters.
   */
  public Double odometer() {
    return this.odometer;
  }

  /**
   * Momentary speed measured by the vehicle, in meters per second.
   */
  public Double speed() {
    return this.speed;
  }

  /**
   * Degrees East, in the WGS-84 coordinate system.
   */
  public double longitude() {
    return this.longitude;
  }

  /**
   * Degrees North, in the WGS-84 coordinate system.
   */
  public double latitude() {
    return this.latitude;
  }

  /**
   * Bearing, in degrees, clockwise from True North, i.e., 0 is North and 90 is East. This can be
   * the compass bearing, or the direction towards the next stop or intermediate location. This
   * should not be deduced from the sequence of previous positions, which clients can compute from
   * previous data.
   */
  public Double bearing() {
    return this.bearing;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeDouble($responseFields[0], odometer);
        writer.writeDouble($responseFields[1], speed);
        writer.writeDouble($responseFields[2], longitude);
        writer.writeDouble($responseFields[3], latitude);
        writer.writeDouble($responseFields[4], bearing);
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString =
          "Position{" + "odometer=" + odometer + ", " + "speed=" + speed + ", " + "longitude="
              + longitude + ", " + "latitude=" + latitude + ", " + "bearing=" + bearing + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Position) {
      Position that = (Position) o;
      return this.odometer.equals(that.odometer) && this.speed.equals(that.speed)
          && Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(that.longitude)
          && Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(that.latitude)
          && this.bearing.equals(that.bearing);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= odometer.hashCode();
      h *= 1000003;
      h ^= speed.hashCode();
      h *= 1000003;
      h ^= Double.valueOf(longitude).hashCode();
      h *= 1000003;
      h ^= Double.valueOf(latitude).hashCode();
      h *= 1000003;
      h ^= bearing.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<Position> {
    @Override
    public Position map(ResponseReader reader) {
      final Double odometer = reader.readDouble($responseFields[0]);
      final Double speed = reader.readDouble($responseFields[1]);
      final double longitude = reader.readDouble($responseFields[2]);
      final double latitude = reader.readDouble($responseFields[3]);
      final Double bearing = reader.readDouble($responseFields[4]);
      return new Position(odometer, speed, longitude, latitude, bearing);
    }
  }
}
