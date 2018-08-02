package io.laegler.fleet.gtfs.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Additional information about the feed itself, including publisher, version, and expiration
 * information. <br>
 * <br>
 * The file contains information about the feed itself, rather than the services that the feed
 * describes. GTFS currently has an agency.txt file to provide information about the agencies that
 * operate the services described by the feed. However, the publisher of the feed is sometimes a
 * different entity than any of the agencies (in the case of regional aggregators). In addition,
 * there are some fields that are really feed-wide settings, rather than agency-wide.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feed_info")
public class FeedInfo {

  @Id
  @Column(name = "feed_publisher_name", nullable = false)
  private String publisherName;

  @Column(name = "feed_publisher_url", nullable = false)
  private String pusblisherUrl;

  @Column(name = "feed_lang", nullable = false)
  private String language;

  @Column(name = "feed_start_date")
  private LocalDate startDate;

  @Column(name = "feed_end_date")
  private LocalDate endDate;

  @Column(name = "feed_version")
  private String version;

}
