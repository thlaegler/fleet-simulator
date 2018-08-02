package io.laegler.fleet.gtfs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.FareRule;

@Repository
@RepositoryRestResource(collectionResourceRel = "fare-rules", path = "gtfs/fare-rules")
public interface GtfsFareRuleJpaRepository extends JpaRepository<FareRule, String> {

}
