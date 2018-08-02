package io.laegler.fleet.gtfs.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One or more transit agencies that provide the data in this feed. Hibernate disables insert
 * batching at the JDBC level transparently if you use an identity identifier generator.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agency")
public class Agency {

  @Id
  @Column(name = "agency_id", nullable = false)
  private String agencyId;

  @Column(name = "agency_name", nullable = false)
  private String name;

  @Column(name = "agency_url", nullable = false)
  private String url;

  @Column(name = "agency_timezone", nullable = false)
  private String timezone;

  @Column(name = "agency_lang", nullable = true)
  private String lang;

  @Column(name = "agency_phone", nullable = true)
  private String phone;

  @Column(name = "fare_url", nullable = true)
  private String fareUrl;

  @JsonManagedReference
  @OneToMany(mappedBy = "agency", fetch = FetchType.LAZY)
  // @JoinColumn(name = "agency_id")
  private Set<Route> routes;
}
