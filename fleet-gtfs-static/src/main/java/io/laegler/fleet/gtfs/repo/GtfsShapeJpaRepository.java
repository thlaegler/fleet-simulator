package io.laegler.fleet.gtfs.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.Shape;
import io.laegler.fleet.gtfs.model.ShapeCompositeId;

@Repository
@RepositoryRestResource(collectionResourceRel = "shapes", path = "/gtfs/shapes")
public interface GtfsShapeJpaRepository extends JpaRepository<Shape, ShapeCompositeId> {

  @Query(
      value = "SELECT * FROM shapes s, trips t WHERE t.trip_id = ?1 AND t.shape_id = s.shape_id ORDER BY s.shape_pt_sequence ASC",
      nativeQuery = true)
  List<Shape> findByTripIdOrderBySequenceAsc(@Param("tripId") String tripId);

  // List<Shape> findByTrips_TripId(@Param("tripId") String tripId);

}
