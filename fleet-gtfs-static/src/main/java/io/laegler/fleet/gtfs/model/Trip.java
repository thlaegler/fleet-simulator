package io.laegler.fleet.gtfs.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One or more transit agencies that provide the data in this feed.
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
@Table(name = "trips")
public class Trip implements Serializable {

  private static final long serialVersionUID = 5145447946859483604L;

  /**
   * trip_id Required The trip_id field contains an ID that identifies a trip. The trip_id is
   * dataset unique.
   */
  @Id
  @Column(name = "trip_id", nullable = false)
  private String tripId;

  @JsonIgnore
  @JsonManagedReference
  @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<StopTime> stopTimes;

  /**
   * route_id Required The route_id field contains an ID that uniquely identifies a route. This
   * value is referenced from the routes.txt file.
   */
  @JsonManagedReference
  @ManyToOne
  @JoinColumn(name = "route_id", nullable = true)
  private Route route;

  /**
   * shape_id Optional The shape_id field contains an ID that defines a shape for the trip. This
   * value is referenced from the shapes.txt file. The shapes.txt file allows you to define how a
   * line should be drawn on the map to represent a trip.
   */
  // @JsonManagedReference
  // @MapsId("shape_id")
  // @Where(clause = "shape_pt_sequence = 1")
  @OneToMany
  // (mappedBy = "shapeCompositeId.shapeId")
  // (fetch = FetchType.LAZY)
  // @LazyCollection(value = LazyCollectionOption.TRUE)
  // @OrderBy("shape_pt_sequence ASC")
  @JoinColumn(name = "shape_id", insertable = false, updatable = false, nullable = true)
  // @JoinColumns({
  // @JoinColumn(name = "shape_id", insertable = false, updatable = false, nullable = true),
  // @JoinColumn(name = "shape_pt_sequence", insertable = false, updatable = false,
  // nullable = true)})
  private Set<Shape> shapes;

  /**
   * service_id Required The service_id contains an ID that uniquely identifies a set of dates when
   * service is available for one or more routes. This value is referenced from the calendar.txt or
   * calendar_dates.txt file.
   */
  @ManyToOne
  @JoinColumn(name = "service_id", nullable = true)
  private Calendar service;

  /**
   * trip_headsign Optional The trip_headsign field contains the text that appears on a sign that
   * identifies the trip's destination to passengers. Use this field to distinguish between
   * different patterns of service in the same route. If the headsign changes during a trip, you can
   * override the trip_headsign by specifying values for the the stop_headsign field in
   * stop_times.txt. See a Google Maps screenshot highlighting the headsign.
   */
  @Column(name = "trip_headsign")
  private String headsign;

  /**
   * trip_short_name Optional The trip_short_name field contains the text that appears in schedules
   * and sign boards to identify the trip to passengers, for example, to identify train numbers for
   * commuter rail trips. If riders do not commonly rely on trip names, please leave this field
   * blank. A trip_short_name value, if provided, should uniquely identify a trip within a service
   * day; it should not be used for destination names or limited/express designations.
   */
  @Column(name = "trip_short_name")
  private String shortName;

  /**
   * block_id Optional The block_id field identifies the block to which the trip belongs. A block
   * consists of two or more sequential trips made using the same vehicle, where a passenger can
   * transfer from one trip to the next just by staying in the vehicle. The block_id must be
   * referenced by two or more trips in trips.txt.
   */
  @Column(name = "block_id")
  private String blockId;
  /**
   * direction_id Optional The direction_id field contains a binary value that indicates the
   * direction of travel for a trip. Use this field to distinguish between bi-directional trips with
   * the same route_id. This field is not used in routing; it provides a way to separate trips by
   * direction when publishing time tables. You can specify names for each direction with the
   * trip_headsign field.<br>
   * <br>
   * For example, you could use the trip_headsign and direction_id fields together to assign a name
   * to travel in each direction for a set of trips. A trips.txt file could contain these rows for
   * use in time tables:
   *
   * <pre>
   *     trip_id,...,trip_headsign,direction_id
   *     1234,...,to Airport,0
   *     1505,...,to Downtown,1
   * </pre>
   *
   * @see DirectionType refer to documentation for more details.
   */
  @Column(name = "direction_id")
  private DirectionType directionType;

  /**
   * wheelchair_accessible Optional
   *
   * <pre>
   *     0 (or empty) - indicates that there is no accessibility information for the trip
   *     1 - indicates that the vehicle being used on this particular trip can accommodate at least one rider in a wheelchair
   *     2 - indicates that no riders in wheelchairs can be accommodated on this trip
   * </pre>
   */
  @Column(name = "trip_type")
  private TripType tripType;

}
