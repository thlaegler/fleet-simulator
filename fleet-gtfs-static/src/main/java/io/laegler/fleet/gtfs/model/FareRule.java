package io.laegler.fleet.gtfs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rules for applying fare information for a transit organization's routes.<br>
 * <br>
 * The fare_rules table allows you to specify how fares in fare_attributes.txt apply to an
 * itinerary. Most fare structures use some combination of the following rules:
 *
 * <pre>
 * Fare depends on origin or destination stations.
 * Fare depends on which zones the itinerary passes through.
 * Fare depends on which route the itinerary uses.
 * </pre>
 *
 * For examples that demonstrate how to specify a fare structure with fare_rules.txt and
 * fare_attributes.txt, see FareExamples in the GoogleTransitDataFeed open source project wiki.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fare_rules")
public class FareRule {

  @Id
  @Column(name = "fare_id")
  private String fareId;

  @ManyToOne
  @JoinColumn(name = "route_id", nullable = false)
  private Route route;

  @Column(name = "origin_id")
  private String originZoneId;

  @Column(name = "destination_id")
  private String destinationZone;

  @Column(name = "contains_id")
  private String containsZoneId;
}
