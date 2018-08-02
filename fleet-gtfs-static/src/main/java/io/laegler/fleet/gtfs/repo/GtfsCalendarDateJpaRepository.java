package io.laegler.fleet.gtfs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.CalendarDate;

@Repository
@RepositoryRestResource(collectionResourceRel = "calendar-dates", path = "gtfs/calendar-dates")
public interface GtfsCalendarDateJpaRepository extends JpaRepository<CalendarDate, String> {

}
