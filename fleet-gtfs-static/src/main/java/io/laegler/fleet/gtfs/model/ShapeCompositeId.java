package io.laegler.fleet.gtfs.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ShapeCompositeId implements Serializable {

  private static final long serialVersionUID = -3767345717317013392L;

  /**
   * shape_id Required The shape_id field contains an ID that uniquely identifies a shape.
   */
  @Column(name = "shape_id", nullable = false)
  private String shapeId;

  /**
   * shape_pt_sequence Required The shape_pt_sequence field associates the latitude and longitude of
   * a shape point with its sequence order along the shape. The values for shape_pt_sequence must be
   * non-negative integers, and they must increase along the trip. For example, if the shape "A_shp"
   * has three points in its definition, the shapes.txt file might contain these rows to define the
   * shape:
   *
   * <pre>
   * A_shp,37.61956,-122.48161,0
   * A_shp,37.64430,-122.41070,6
   * A_shp,37.65863,-122.30839,11
   * </pre>
   */
  @Column(name = "shape_pt_sequence", nullable = false)
  private int sequence;

}
