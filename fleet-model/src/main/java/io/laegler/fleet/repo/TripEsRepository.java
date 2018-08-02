package io.laegler.fleet.repo;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.model.Status;
import io.laegler.fleet.model.Trip;

@Repository
@RepositoryRestResource(collectionResourceRel = "trips2", path = "trips2")
public interface TripEsRepository extends ElasticsearchRepository<Trip, String> {

  List<Trip> findByProviderId(String providerId);

  List<Trip> findByStatusInAndEtaAfterAndStartAfter(List<Status> stati, LocalDateTime minEta,
      LocalDateTime minStart);

  List<Trip> findByStatusIn(List<Status> stati);

  List<Trip> findByProviderIdAndStatusInAndEtaAfterAndStartAfter(String providerId,
      List<Status> stati, LocalDateTime minEta, LocalDateTime minStart);

  List<Trip> findByProviderIdAndStatusIn(String providerId, List<Status> stati);

}
