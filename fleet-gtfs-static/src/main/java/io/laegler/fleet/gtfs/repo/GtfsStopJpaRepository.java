package io.laegler.fleet.gtfs.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.Stop;

@Repository
@RepositoryRestResource(collectionResourceRel = "stops", path = "gtfs/stops")
public interface GtfsStopJpaRepository extends JpaRepository<Stop, String> {

  List<Stop> findByStopTimes_Trip_TripIdOrderByStopTimes_StopSequenceAsc(
      @Param("tripId") String tipId);

}
