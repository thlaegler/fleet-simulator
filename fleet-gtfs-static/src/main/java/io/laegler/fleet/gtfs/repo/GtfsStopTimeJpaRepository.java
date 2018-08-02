package io.laegler.fleet.gtfs.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.StopTime;

@Repository
@RepositoryRestResource(collectionResourceRel = "stop-times", path = "/gtfs/stoptimes")
public interface GtfsStopTimeJpaRepository extends JpaRepository<StopTime, Integer> {

  List<StopTime> findByTripTripId(String tripId);

  List<StopTime> findByStopStopId(String stopId);

  List<StopTime> findByTrip_TripIdOrderByStopSequenceAsc(String tripId);

}
