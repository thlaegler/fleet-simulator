package io.laegler.fleet.gtfs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.Transfer;

@Repository
@RepositoryRestResource(collectionResourceRel = "transfers", path = "gtfs/transfers")
public interface GtfsTransferJpaRepository extends JpaRepository<Transfer, String> {

}
