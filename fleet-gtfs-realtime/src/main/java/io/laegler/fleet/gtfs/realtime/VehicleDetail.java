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

@ApiModel(description = "Identification information for the vehicle performing the trip.")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class VehicleDetail implements Serializable {

  private static final long serialVersionUID = -4336241621803889838L;

  static final ResponseField[] $responseFields =
      {ResponseField.forString("label", "label", null, true, emptyList()),
          ResponseField.forString("id", "id", null, true, emptyList()),
          ResponseField.forString("licensePlate", "licensePlate", null, true, emptyList())};

  @ApiModelProperty("Internal system identification of the vehicle. Should be unique per vehicle, and is used for tracking the vehicle as it proceeds through the system. This id should not be made visible to the end-user; for that purpose use the label field")
  private String id;

  @ApiModelProperty("User visible label, i.e., something that must be shown to the passenger to help identify the correct vehicle.")
  private String label;

  @ApiModelProperty("The license plate of the vehicle.")
  private String licensePlate;

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  /**
   * User visible label, i.e., something that must be shown to the passenger to help identify the
   * correct vehicle.
   */
  public String label() {
    return this.label;
  }

  /**
   * Internal system identification of the vehicle. Should be unique per vehicle, and is used for
   * tracking the vehicle as it proceeds through the system. This id should not be made visible to
   * the end-user; for that purpose use the label field
   */
  public String id() {
    return this.id;
  }

  /**
   * The license plate of the vehicle.
   */
  public String licensePlate() {
    return this.licensePlate;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeString($responseFields[0], label);
        writer.writeString($responseFields[1], id);
        writer.writeString($responseFields[2], licensePlate);
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "VehicleDetail{" + "label=" + label + ", " + "id=" + id + ", " + "licensePlate="
          + licensePlate + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof VehicleDetail) {
      VehicleDetail that = (VehicleDetail) o;
      return this.label.equals(that.label) && this.id.equals(that.id)
          && this.licensePlate.equals(that.licensePlate);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= label.hashCode();
      h *= 1000003;
      h ^= id.hashCode();
      h *= 1000003;
      h ^= licensePlate.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<VehicleDetail> {
    @Override
    public VehicleDetail map(ResponseReader reader) {
      final String label = reader.readString($responseFields[0]);
      final String id = reader.readString($responseFields[1]);
      final String licensePlate = reader.readString($responseFields[2]);
      return new VehicleDetail(label, id, licensePlate);
    }
  }
}
