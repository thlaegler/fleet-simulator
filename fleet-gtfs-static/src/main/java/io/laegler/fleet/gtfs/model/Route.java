package io.laegler.fleet.gtfs.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.laegler.fleet.gtfs.GtfsRouteTypeConverter;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transit routes. A route is a group of trips that are displayed to riders as a single service.
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
@Table(name = "routes")
public class Route implements Serializable {

  private static final long serialVersionUID = 5145427936859483604L;

  @Id
  @Column(name = "route_id")
  private String routeId;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "agency_id", referencedColumnName = "agency_id", nullable = false)
  private Agency agency;

  @JsonBackReference
  @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
  private Set<Trip> trips;

  @Column(name = "route_short_name", nullable = false)
  private String shortName;

  @Column(name = "route_long_name", nullable = true)
  private String longName;

  @Column(name = "route_type")
  @Convert(converter = GtfsRouteTypeConverter.class)
  private RouteType type;

  @Column(name = "route_desc")
  private String desc;

  @Column(name = "route_url")
  private String url;

  @Column(name = "route_color")
  private String hexPathColor;

  @Column(name = "route_text_color")
  private String hexTextColor;
}
