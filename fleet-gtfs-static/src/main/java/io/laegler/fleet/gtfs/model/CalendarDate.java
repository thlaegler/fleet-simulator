package io.laegler.fleet.gtfs.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import io.laegler.fleet.gtfs.GtfsLocalDateConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Exceptions for the service IDs defined in the calendar.txt file. If calendar_dates.txt includes
 * ALL dates of service, this file may be specified instead of calendar.txt.<br>
 * <br>
 * The calendar_dates table allows you to explicitly activate or disable service IDs by date. You
 * can use it in two ways. Recommended: Use calendar_dates.txt in conjunction with calendar.txt,
 * where calendar_dates.txt defines any exceptions to the default service categories defined in the
 * calendar.txt file. If your service is generally regular, with a few changes on explicit dates
 * (for example, to accomodate special event services, or a school schedule), this is a good
 * approach. Alternate: Omit calendar.txt, and include ALL dates of service in calendar_dates.txt.
 * If your schedule varies most days of the month, or you want to programmatically output service
 * dates without specifying a normal weekly schedule, this approach may be preferable.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calendar_dates")
public class CalendarDate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long calendarDateId;

  /**
   * service_id Required The service_id contains an ID that uniquely identifies a set of dates when
   * a service exception is available for one or more routes. Each (service_id, date) pair can only
   * appear once in calendar_dates.txt. If the a service_id value appears in both the calendar.txt
   * and calendar_dates.txt files, the information in calendar_dates.txt modifies the service
   * information specified in calendar.txt. This field is referenced by the trips.txt file.
   */
  @ManyToOne
  @JoinColumn(name = "service_id", referencedColumnName = "service_id", nullable = false)
  private Calendar service;

  /**
   * date Required The date field specifies a particular date when service availability is different
   * than the norm. You can use the exception_type field to indicate whether service is available on
   * the specified date. The date field's value should be in YYYYMMDD format.
   */
  @Convert(converter = GtfsLocalDateConverter.class)
  @Column(name = "date", columnDefinition = "varchar(255)", nullable = false)
  private LocalDate date;

  /**
   * exception_type Required The exception_type indicates whether service is available on the date
   * specified in the date field.
   * 
   * @see ExceptionType refer to documentation for more clarification.
   */
  @Column(name = "exception_type")
  private ExceptionType exceptionType;

}
