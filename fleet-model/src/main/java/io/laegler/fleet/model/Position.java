package io.laegler.fleet.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApiModel
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
// @AllArgsConstructor
@Validated
@Embeddable
public class Position implements Serializable {

  private static final long serialVersionUID = 1938774642856549142L;

  @ApiModelProperty(value = "Latitude", example = "-36.88194728045074")
  @Field(type = FieldType.Double)
  @NotNull
  private double lat;

  @ApiModelProperty(value = "Longitude", example = "174.65188669561232")
  @Field(type = FieldType.Double)
  @NotNull
  private double lng;

  @ApiModelProperty(value = "Latitude/Longitude", example = "-36.881, 174.651", hidden = true)
  @GeoPointField
  // @Transient
  private String location;

  @ApiModelProperty(name = "place_id",
      value = "Google Place ID - Place_id is a field returned from Google Place API. it's unique to a location.// based on place_id, we will retrieve (from Google Place Service) all the needed information for the below requests",
      example = "ChIJORy6nXuwj4ARz3b1NVL1Hw4", required = false)
  @Field(type = Keyword)
  private String placeId;

  @ApiModelProperty(value = "Address String", example = "Ponsonby, Auckland, New Zealand")
  @Field(type = Text)
  @Transient
  private String address;

  // Getter/Setter for Elasticsearch GeoField (expected field name is 'lon' not 'lng')
  public Position(double lat, double lng, String location, String placeId, String address) {
    super();
    this.lat = lat;
    this.lng = lng;
    this.location = location;
    this.placeId = placeId;
    this.address = address;

    if (this.location == null && this.lat != 0.0 && this.lng != 0.0) {
      setLat(this.lat);
      setLng(this.lng);
    } else {
      setLocation(this.location);
    }
  }

  public void setLocation(String location) {
    this.location = location;
    if (location != null) {
      try {
        String[] split = location.split(",");
        this.lat = Double.parseDouble(split[0]);
        this.lng = Double.parseDouble(split[1]);
      } catch (NumberFormatException ex) {
        log.warn("Cannot split location string", ex);
      }
    }
  }

  public void setLat(double lat) {
    this.lat = lat;
    if (location == null) {
      this.location = "0.0,0.0";
    }
    try {
      String[] split = location.split(",");
      this.location = this.lat + "," + split[1];
    } catch (NumberFormatException ex) {
      log.warn("Cannot split location string", ex);
    }
  }

  public void setLng(double lng) {
    this.lng = lng;
    if (location == null) {
      this.location = "0.0,0.0";
    }
    try {
      String[] split = location.split(",");
      this.location = split[0] + "," + this.lng;
    } catch (NumberFormatException ex) {
      log.warn("Cannot split lat/lng string", ex);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    Position p = (Position) obj;
    return new EqualsBuilder().append(lat, p.lat).append(lng, p.lng).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(lat).append(lng).toHashCode();
  }

}
