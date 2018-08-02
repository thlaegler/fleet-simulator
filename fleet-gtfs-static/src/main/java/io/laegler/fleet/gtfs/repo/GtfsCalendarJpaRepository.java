package io.laegler.fleet.gtfs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.Calendar;

@Repository
@RepositoryRestResource(collectionResourceRel = "calendars", path = "gtfs/calendars")
public interface GtfsCalendarJpaRepository extends JpaRepository<Calendar, String> {

}
