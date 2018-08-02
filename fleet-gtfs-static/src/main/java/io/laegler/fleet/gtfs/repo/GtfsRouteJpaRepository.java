package io.laegler.fleet.gtfs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.Route;

@Repository
@RepositoryRestResource(collectionResourceRel = "routes", path = "gtfs/routes")
public interface GtfsRouteJpaRepository extends JpaRepository<Route, String> {

}
