package io.laegler.fleet.gtfs.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.data.annotation.Id;
import io.laegler.fleet.gtfs.GtfsLocalDateConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dates for service IDs using a weekly schedule. Specify when service starts and ends, as well as
 * days of the week where service is available.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calendar")
public class Calendar {

  /**
   * service_id Required The service_id contains an ID that uniquely identifies a set of dates when
   * service is available for one or more routes. Each service_id value can appear at most once in a
   * calendar.txt file. This value is dataset unique. It is referenced by the trips.txt file.
   */
  @Id
  @Column(name = "service_id", nullable = false)
  private String serviceId;

  // @OneToMany(fetch = FetchType.LAZY)
  // @LazyCollection(value = LazyCollectionOption.TRUE)
  // private Set<CalendarDate> calendarDates;

  /**
   * monday Required The monday field contains a binary value that indicates whether the service is
   * valid for all Mondays.
   *
   * <pre>
   *     A value of 1 indicates that service is available for all Mondays in the date range. (The date range is specified using the start_date and end_date fields.)
   *     A value of 0 indicates that service is not available on Mondays in the date range.
   * </pre>
   *
   * Note: You may list exceptions for particular dates, such as holidays, in the calendar_dates.txt
   * file.
   */
  @Column(name = "monday")
  private boolean monday;

  /**
   * tuesday Required The tuesday field contains a binary value that indicates whether the service
   * is valid for all Tuesdays.
   *
   * <pre>
   *     A value of 1 indicates that service is available for all Tuesdays in the date range. (The date range is specified using the start_date and end_date fields.)
   *     A value of 0 indicates that service is not available on Tuesdays in the date range.
   * </pre>
   *
   * Note: You may list exceptions for particular dates, such as holidays, in the calendar_dates.txt
   * file.
   */
  @Column(name = "tuesday")
  private boolean tuesday;

  /**
   * wednesday Required The wednesday field contains a binary value that indicates whether the
   * service is valid for all Wednesdays.
   *
   * <pre>
   * A value of 1 indicates that service is available for all Wednesdays in the date range. (The
   * date range is specified using the start_date and end_date fields.) A value of 0 indicates that
   * service is not available on Wednesdays in the date range. Note: You may list exceptions for
   * particular dates, such as holidays, in the calendar_dates.txt file.
   */
  @Column(name = "wednesday")
  private boolean wednesday;

  /**
   * thursday Required The thursday field contains a binary value that indicates whether the service
   * is valid for all Thursdays.
   *
   * <pre>
   * A value of 1 indicates that service is available for all Thursdays in the date range. (The date
   * range is specified using the start_date and end_date fields.) A value of 0 indicates that
   * service is not available on Thursdays in the date range. Note: You may list exceptions for
   * particular dates, such as holidays, in the calendar_dates.txt file.
   */
  @Column(name = "thursday")
  private boolean thursday;

  /**
   * friday Required The friday field contains a binary value that indicates whether the service is
   * valid for all Fridays.
   *
   * <pre>
   *     A value of 1 indicates that service is available for all Fridays in the date range. (The date range is specified using the start_date and end_date fields.)
   *     A value of 0 indicates that service is not available on Fridays in the date range.
   * </pre>
   *
   * Note: You may list exceptions for particular dates, such as holidays, in the calendar_dates.txt
   * file
   */
  @Column(name = "friday")
  private boolean friday;

  /**
   * saturday Required The saturday field contains a binary value that indicates whether the service
   * is valid for all Saturdays.
   *
   * <pre>
   *     A value of 1 indicates that service is available for all Saturdays in the date range. (The date range is specified using the start_date and end_date fields.)
   *     A value of 0 indicates that service is not available on Saturdays in the date range.
   * </pre>
   *
   * Note: You may list exceptions for particular dates, such as holidays, in the calendar_dates.txt
   * file.
   */
  @Column(name = "saturday")
  private boolean saturday;

  /**
   * sunday Required The sunday field contains a binary value that indicates whether the service is
   * valid for all Sundays.
   *
   * <pre>
   *     A value of 1 indicates that service is available for all Sundays in the date range. (The date range is specified using the start_date and end_date fields.)
   *     A value of 0 indicates that service is not available on Sundays in the date range.
   * </pre>
   *
   * Note: You may list exceptions for particular dates, such as holidays, in the calendar_dates.txt
   * file.
   */
  @Column(name = "sunday")
  private boolean sunday;

  /**
   * start_date Required The start_date field contains the start date for the service. The
   * start_date field's value should be in YYYYMMDD format.
   */
  @Convert(converter = GtfsLocalDateConverter.class)
  @Column(name = "start_date", columnDefinition = "varchar(255)")
  private LocalDate startDate;

  /**
   * end_date Required The end_date field contains the end date for the service. This date is
   * included in the service interval. The end_date field's value should be in YYYYMMDD format.
   */
  @Convert(converter = GtfsLocalDateConverter.class)
  @Column(name = "end_date", columnDefinition = "varchar(255)")
  private LocalDate endDate;

}
