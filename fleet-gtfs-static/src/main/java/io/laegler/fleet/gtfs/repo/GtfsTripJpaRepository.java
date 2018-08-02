package io.laegler.fleet.gtfs.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.Trip;

@Repository
@RepositoryRestResource(collectionResourceRel = "trips", path = "gtfs/trips")
public interface GtfsTripJpaRepository extends JpaRepository<Trip, String> {

  List<Trip> findByRouteRouteId(String routeId);

  // List<Shape> findByTripIdOrderByShapes_Sequence();

}
