package io.laegler.fleet.gtfs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.FareAttribute;

@Repository
@RepositoryRestResource(collectionResourceRel = "fare-attributes", path = "gtfs/fare-attributes")
public interface GtfsFareAttributeJpaRepository extends JpaRepository<FareAttribute, String> {

}
