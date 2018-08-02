package io.laegler.fleet.gtfs;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.springframework.util.CollectionUtils.isEmpty;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.persistence.EntityManager;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import io.laegler.fleet.gtfs.model.Agency;
import io.laegler.fleet.gtfs.model.Calendar;
import io.laegler.fleet.gtfs.model.CalendarDate;
import io.laegler.fleet.gtfs.model.DirectionType;
import io.laegler.fleet.gtfs.model.ExceptionType;
import io.laegler.fleet.gtfs.model.FareAttribute;
import io.laegler.fleet.gtfs.model.FareRule;
import io.laegler.fleet.gtfs.model.Frequency;
import io.laegler.fleet.gtfs.model.Route;
import io.laegler.fleet.gtfs.model.RouteType;
import io.laegler.fleet.gtfs.model.Shape;
import io.laegler.fleet.gtfs.model.ShapeCompositeId;
import io.laegler.fleet.gtfs.model.Stop;
import io.laegler.fleet.gtfs.model.StopLocationType;
import io.laegler.fleet.gtfs.model.StopTime;
import io.laegler.fleet.gtfs.model.Transfer;
import io.laegler.fleet.gtfs.model.TransferType;
import io.laegler.fleet.gtfs.model.Trip;
import io.laegler.fleet.gtfs.repo.GtfsAgencyJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsCalendarDateJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsCalendarJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsFareAttributeJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsFareRuleJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsFeedInfoJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsFrequencyJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsRouteJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsShapeJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsStopJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsStopTimeJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsTransferJpaRepository;
import io.laegler.fleet.gtfs.repo.GtfsTripJpaRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO: Read and unzip gtfs.zip file
 */
@Slf4j
@Component
public class GtfsLoader {

  private static final String GTFS_FOLDER = "gtfs/";

  @Autowired
  private EntityManager entityManager;

  private GtfsLocalDateConverter dateConverter = new GtfsLocalDateConverter();

  private GtfsLocalTimeConverter timeConverter = new GtfsLocalTimeConverter();

  @Autowired
  private GtfsStopJpaRepository stopRepo;

  @Autowired
  private GtfsStopTimeJpaRepository stopTimeRepo;

  @Autowired
  private GtfsTransferJpaRepository transferRepo;

  @Autowired
  private GtfsShapeJpaRepository shapeRepo;

  @Autowired
  private GtfsTripJpaRepository tripRepo;

  @Autowired
  private GtfsAgencyJpaRepository agencyRepo;

  @Autowired
  private GtfsFeedInfoJpaRepository feedInfoRepo;

  @Autowired
  private GtfsCalendarDateJpaRepository calendarDateRepo;

  @Autowired
  private GtfsCalendarJpaRepository calendarRepo;

  @Autowired
  private GtfsFareRuleJpaRepository fareRuleRepo;

  @Autowired
  private GtfsFareAttributeJpaRepository fareAttributeRepo;

  @Autowired
  private GtfsFrequencyJpaRepository frequencyRepo;

  @Autowired
  private GtfsRouteJpaRepository routeRepo;

  @SuppressWarnings("unused")
  public void loadGtfs() {
    try {
      // unzipArchive();

      List<Agency> agencies = parseAgencies();
      List<Calendar> calendar = parseCalendar();
      // List<CalendarDate> calendarDates = parseCalendarDates();
      List<Shape> shapes = parseShapes();
      List<Stop> stops = parseStops();
      List<Trip> trips = parseTrips();
      List<StopTime> stopTimes = parseStopTimes();
      List<Transfer> transfer = parseTransfer();
      List<Route> routes = parseRoutes();
      List<FareAttribute> fareAttributes = parseFareAttributes();
      List<FareRule> fareRules = parseFareRules();
      List<Frequency> frequencies = parseFrequencies();
    } catch (IOException ex) {
      log.error("Cannot load GTFS files", ex);
    } catch (URISyntaxException ex) {
      log.error("Cannot load GTFS files", ex);
    }
  }

  public void unzipArchive() throws IOException {
    String archiveFilename = "gtfs.zip";
    byte[] buffer = new byte[1024];
    // InputStream inputStream = new FileInputStream(new File(GTFS_FOLDER + archiveFilename));
    InputStream inputStream =
        getClass().getClassLoader().getResource(GTFS_FOLDER + archiveFilename).openStream();
    ZipInputStream zis = new ZipInputStream(inputStream, Charset.forName("UTF-8"));
    ZipEntry zipEntry = zis.getNextEntry();
    while (zipEntry != null) {
      String filename = zipEntry.getName();
      if (filename.length() > 3) {
        URL url = getClass().getResource(GTFS_FOLDER + filename);
        File newFile = new File(url.getPath());
        // File newFile = new File(GTFS_FOLDER + filename);
        FileOutputStream fos = new FileOutputStream(newFile);
        int len;
        while ((len = zis.read(buffer)) > 0) {
          fos.write(buffer, 0, len);
        }
        fos.close();
      }
      zipEntry = zis.getNextEntry();
    }
    zis.closeEntry();
    zis.close();
  }

  @Transactional
  private List<Calendar> parseCalendar() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("calendar.txt");
    if (!csvRecords.isEmpty()) {
      List<Calendar> calendar = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> Calendar.builder().serviceId(csvRecord.get("service_id"))
              // .calendarDates(null)
              .endDate(toDate(csvRecord.get("end_date"))).build())
          .filter(Objects::nonNull).collect(toList());
      if (!isEmpty(calendar)) {
        return calendarRepo.saveAll(calendar);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<CalendarDate> parseCalendarDates() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("calendar_dates.txt");
    if (!csvRecords.isEmpty()) {
      List<CalendarDate> calendarDates = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> CalendarDate.builder()
              .service(entityManager.getReference(Calendar.class, csvRecord.get("service_id")))
              .date(toDate(csvRecord.get("date")))
              .exceptionType(toExceptionType(csvRecord.get("exception_type"))).build())
          .filter(Objects::nonNull).collect(toList());
      if (!isEmpty(calendarDates)) {
        return calendarDateRepo.saveAll(calendarDates);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<Agency> parseAgencies() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("agency.txt");
    if (!csvRecords.isEmpty()) {
      List<Agency> agencies = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> Agency.builder().agencyId(csvRecord.get("agency_id"))
              .name(csvRecord.get("agency_name")).url(csvRecord.get("agency_url"))
              .timezone(csvRecord.get("agency_timezone")).phone(csvRecord.get("agency_phone"))
              .lang(csvRecord.get("agency_lang")).build())
          .filter(Objects::nonNull).collect(toList());
      if (!isEmpty(agencies)) {
        return agencyRepo.saveAll(agencies);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<Shape> parseShapes() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("shapes.txt");
    if (!csvRecords.isEmpty()) {
      List<Shape> shapes = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> Shape.builder()
              .shapeCompositeId(ShapeCompositeId.builder().shapeId(csvRecord.get("shape_id"))
                  .sequence(toInt(csvRecord.get("shape_pt_sequence"))).build())
              .latitude(toDouble(csvRecord.get("shape_pt_lat")))
              .longitude(toDouble(csvRecord.get("shape_pt_lon")))
              .distanceTraveled(toDouble(csvRecord.get("shape_dist_traveled"))).build())
          .filter(Objects::nonNull).collect(toList());
      if (!isEmpty(shapes)) {
        return shapeRepo.saveAll(shapes);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<Stop> parseStops() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("stops.txt");
    if (!csvRecords.isEmpty()) {
      List<Stop> stops = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> Stop.builder().stopId(csvRecord.get("stop_id"))
              .name(csvRecord.get("stop_name")).description(csvRecord.get("stop_desc"))
              .latitude(toDouble(csvRecord.get("stop_lat")))
              .longitude(toDouble(csvRecord.get("stop_lon"))).code(csvRecord.get("stop_code"))
              .locationType(toStopLocationType(csvRecord.get("location_type")))
              .parentStation(csvRecord.get("parent_station")).build())
          .collect(toList());
      if (!isEmpty(stops)) {
        return stopRepo.saveAll(stops);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<Trip> parseTrips() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("trips.txt");
    if (!csvRecords.isEmpty()) {
      List<Trip> trips = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> Trip.builder().tripId(csvRecord.get("trip_id"))
              .blockId(csvRecord.get("block_id"))
              .directionType(toDirectionType(csvRecord.get("direction_id")))
              // TODO
              .build())
          .collect(toList());
      if (!isEmpty(trips)) {
        return tripRepo.saveAll(trips);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<StopTime> parseStopTimes() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("stop_times.txt");
    if (!csvRecords.isEmpty()) {
      List<StopTime> stopTimes = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> StopTime.builder()
              .stop(entityManager.getReference(Stop.class, csvRecord.get("stop_id")))
              .trip(entityManager.getReference(Trip.class, csvRecord.get("trip_id")))
              .arrivalTime(toTime(csvRecord.get("trip_id"))).build())
          .filter(Objects::nonNull).collect(toList());
      if (!isEmpty(stopTimes)) {
        return stopTimeRepo.saveAll(stopTimes);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<Transfer> parseTransfer() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("transfer.txt");
    if (!csvRecords.isEmpty()) {
      List<Transfer> transfers = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> Transfer.builder()
              .fromStop(entityManager.getReference(Stop.class, csvRecord.get("from_stop_id")))
              .toStop(entityManager.getReference(Stop.class, csvRecord.get("to_stop_id")))
              .transferType(toTransferType(csvRecord.get("transfer_type")))
              .minTransferTimeSecs(toLong(csvRecord.get("min_transfer_time"))).build())
          .filter(Objects::nonNull).collect(toList());
      if (!isEmpty(transfers)) {
        return transferRepo.saveAll(transfers);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<Route> parseRoutes() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("transfer.txt");
    if (!csvRecords.isEmpty()) {
      List<Route> routes = csvRecords.stream().map(csvRecord -> Route.builder()
          .routeId(csvRecord.get("route_id")).shortName(csvRecord.get("route_short_name"))
          .longName(csvRecord.get("route_long_name")).type(toRouteType(csvRecord.get("route_type")))
          .agency(entityManager.getReference(Agency.class, csvRecord.get("agency_id")))
          .hexTextColor(csvRecord.get("route_text_color"))
          .hexPathColor(csvRecord.get("route_color")).build()).filter(Objects::nonNull)
          .collect(toList());
      if (!isEmpty(routes)) {
        return routeRepo.saveAll(routes);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<Frequency> parseFrequencies() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("frequencies.txt");
    if (!csvRecords.isEmpty()) {
      List<Frequency> frequencies = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> Frequency.builder()
              .trip(entityManager.getReference(Trip.class, csvRecord.get("trip_id")))
              // TODO
              .build())
          .filter(Objects::nonNull).collect(toList());
      if (!isEmpty(frequencies)) {
        return frequencyRepo.saveAll(frequencies);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<FareAttribute> parseFareAttributes() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("fare_attributes.txt");
    if (!csvRecords.isEmpty()) {
      List<FareAttribute> fareAttributes = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> FareAttribute.builder().fareId(csvRecord.get("fare_id"))
              // .shortName(csvRecord.get("route_short_name"))
              // .longName(csvRecord.get("route_long_name"))
              // .type(toRouteType(csvRecord.get("route_type")))
              // .agency(entityManager.getReference(Agency.class, csvRecord.get("agency_id")))
              // .hexTextColor(csvRecord.get("route_text_color"))
              // .hexPathColor(csvRecord.get("route_color"))
              .build())
          .filter(Objects::nonNull).collect(toList());
      if (!isEmpty(fareAttributes)) {
        return fareAttributeRepo.saveAll(fareAttributes);
      }
    }
    return emptyList();
  }

  @Transactional
  private List<FareRule> parseFareRules() throws IOException, URISyntaxException {
    List<CSVRecord> csvRecords = readGtfsFile("fare_fules.txt");
    if (!csvRecords.isEmpty()) {
      List<FareRule> fareRules = csvRecords.stream().filter(Objects::nonNull)
          .map(csvRecord -> FareRule.builder().fareId(csvRecord.get("fare_id"))
              // .shortName(csvRecord.get("route_short_name"))
              // .longName(csvRecord.get("route_long_name"))
              // .type(toRouteType(csvRecord.get("route_type")))
              // .agency(entityManager.getReference(Agency.class, csvRecord.get("agency_id")))
              // .hexTextColor(csvRecord.get("route_text_color"))
              // .hexPathColor(csvRecord.get("route_color"))
              .build())
          .filter(Objects::nonNull).collect(toList());
      if (!isEmpty(fareRules)) {
        return fareRuleRepo.saveAll(fareRules);
      }
    }
    return emptyList();
  }

  private List<CSVRecord> readGtfsFile(String filename) throws IOException, URISyntaxException {
    CSVParser csvParser = null;
    try {
      InputStream inputStream;
      if (new File(GTFS_FOLDER + filename).exists()) {
        inputStream = new FileInputStream(new File(GTFS_FOLDER + filename));
      } else if (getClass().getClassLoader().getResource(GTFS_FOLDER + filename) != null) {
        inputStream = getClass().getClassLoader().getResource(GTFS_FOLDER + filename).openStream();
      } else {
        return emptyList();
      }
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      Reader reader = new BufferedReader(inputStreamReader);
      csvParser = new CSVParser(reader,
          DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

      List<CSVRecord> csvRecords = csvParser.getRecords();
      csvParser.close();
      return csvRecords.stream().filter(Objects::nonNull).collect(toList());
    } catch (Exception ex) {
      log.error("Cannot read gtfs file " + filename, ex);
    } finally {
      if (csvParser != null) {
        csvParser.close();
      }
    }
    return emptyList();
  }

  private Double toDouble(String csvString) {
    try {
      return Double.parseDouble(csvString);
    } catch (Exception ex) {
      log.warn("Cannot resolve Double {}", csvString);
    }
    return null;
  }

  private Long toLong(String csvString) {
    try {
      return Long.parseLong(csvString);
    } catch (Exception ex) {
      log.warn("Cannot resolve Long {}", csvString);
    }
    return null;
  }

  private LocalDate toDate(String csvString) {
    return dateConverter.convertToEntityAttribute(csvString);
  }

  private LocalTime toTime(String csvString) {
    return timeConverter.convertToEntityAttribute(csvString);
  }

  private Integer toInt(String csvString) {
    try {
      return Integer.parseInt(csvString);
    } catch (Exception ex) {
      log.warn("Cannot resolve Integer {}", csvString);
    }
    return null;
  }

  private StopLocationType toStopLocationType(String csvString) {
    try {
      return StopLocationType.fromCode(Integer.parseInt(csvString));
    } catch (Exception ex) {
      log.warn("Cannot resolve StopLocationType {}", csvString);
    }
    return null;
  }

  private TransferType toTransferType(String csvString) {
    try {
      return TransferType.fromCode(Integer.parseInt(csvString));
    } catch (Exception ex) {
      log.warn("Cannot resolve TransferType {}", csvString);
    }
    return null;
  }

  private DirectionType toDirectionType(String csvString) {
    try {
      return DirectionType.fromCode(Integer.parseInt(csvString));
    } catch (Exception ex) {
      log.warn("Cannot resolve DirectionType {}", csvString);
    }
    return null;
  }

  private RouteType toRouteType(String csvString) {
    try {
      return RouteType.fromCode(Integer.parseInt(csvString));
    } catch (Exception ex) {
      log.warn("Cannot resolve RouteType {}", csvString);
    }
    return null;
  }

  private ExceptionType toExceptionType(String csvString) {
    try {
      return ExceptionType.fromCode(Integer.parseInt(csvString));
    } catch (Exception ex) {
      log.warn("Cannot resolve ExceptionType {}", csvString);
    }
    return null;
  }

}
