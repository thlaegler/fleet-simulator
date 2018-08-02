package io.laegler.fleet.gtfs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.Agency;

@Repository
@RepositoryRestResource(collectionResourceRel = "agencies", path = "gtfs/agencies")
public interface GtfsAgencyJpaRepository extends JpaRepository<Agency, String> {

}
