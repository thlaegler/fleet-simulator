package io.laegler.fleet.gtfs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.Frequency;

@Repository
@RepositoryRestResource(collectionResourceRel = "frequencies", path = "gtfs/frequencies")
public interface GtfsFrequencyJpaRepository extends JpaRepository<Frequency, String> {

}
