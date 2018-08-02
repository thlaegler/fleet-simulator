package io.laegler.fleet.gtfs.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rules for drawing lines on a map to represent a transit organization's routes.
 */
@ApiModel
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shapes")
public class Shape implements Serializable {

  private static final long serialVersionUID = 5185447946859483604L;

  @EmbeddedId
  private ShapeCompositeId shapeCompositeId;

  // @OneToMany
  // (mappedBy = "shapeCompositeId.shapeId")
  // @JoinColumn(name = "shape_id", insertable = false, updatable = false, nullable = true)
  // @JoinColumns({
  // @JoinColumn(name = "shape_id", insertable = false, updatable = false, nullable = true),
  // @JoinColumn(name = "shape_pt_sequence", insertable = false, updatable = false,
  // nullable = true)})
  // private Set<Trip> trips;

  /**
   * shape_dist_traveled Optional When used in the shapes.txt file, the shape_dist_traveled field
   * positions a shape point as a distance traveled along a shape from the first shape point. The
   * shape_dist_traveled field represents a real distance traveled along the route in units such as
   * feet or kilometers. This information allows the trip planner to determine how much of the shape
   * to draw when showing part of a trip on the map. The values used for shape_dist_traveled must
   * increase along with shape_pt_sequence: they cannot be used to show reverse travel along a
   * route. The units used for shape_dist_traveled in the shapes.txt file must match the units that
   * are used for this field in the stop_times.txt file. For example, if a bus travels along the
   * three points defined above for A_shp, the additional shape_dist_traveled values (shown here in
   * kilometers) would look like this:
   *
   * <pre>
   * A_shp,37.61956,-122.48161,0,0
   * A_shp,37.64430,-122.41070,6,6.8310
   * A_shp,37.65863,-122.30839,11,15.8765
   * </pre>
   */
  @Column(name = "shape_dist_traveled")
  private Double distanceTraveled;

  /**
   * shape_pt_lat Required The shape_pt_lat field associates a shape point's latitude with a shape
   * ID. The field value must be a valid WGS 84 latitude. Each row in shapes.txt represents a shape
   * point in your shape definition. For example, if the shape "A_shp" has three points in its
   * definition, the shapes.txt file might contain these rows to define the shape:
   *
   * <pre>
   * A_shp,37.61956,-122.48161,0
   * A_shp,37.64430,-122.41070,6
   * A_shp,37.65863,-122.30839,11
   * </pre>
   *
   * shape_pt_lon Required The shape_pt_lon field associates a shape point's longitude with a shape
   * ID. The field value must be a valid WGS 84 longitude value from -180 to 180. Each row in
   * shapes.txt represents a shape point in your shape definition. For example, if the shape "A_shp"
   * has three points in its definition, the shapes.txt file might contain these rows to define the
   * shape:
   *
   * <pre>
   * A_shp,37.61956,-122.48161,0
   * A_shp,37.64430,-122.41070,6
   * A_shp,37.65863,-122.30839,11
   * </pre>
   */
  @Column(name = "shape_pt_lat")
  private Double latitude;

  @Column(name = "shape_pt_lon")
  private Double longitude;

}
