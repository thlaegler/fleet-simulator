package io.laegler.fleet.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.model.Journey;

@Repository
@RepositoryRestResource(collectionResourceRel = "journeys", path = "journeys")
public interface JourneyEsRepository extends ElasticsearchRepository<Journey, String> {

  Optional<Journey> findFirstByTripIds(String tripId);

  List<Journey> findByProviderId(String providerId);

  Optional<Journey> findFirstByGtfsTripIdAndGtfsRouteId(String gtfsTripId, String gtfsRouteId);

  Optional<Journey> findFirstByBookingId(String bookingId);

  List<Journey> findByActiveTrue();

}
