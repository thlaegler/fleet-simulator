package io.laegler.fleet.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.model.Status;
import io.laegler.fleet.model.Vehicle;

@Repository
@RepositoryRestResource(collectionResourceRel = "vehicles", path = "vehicles")
public interface VehicleEsRepository extends ElasticsearchRepository<Vehicle, String> {

  List<Vehicle> findByProviderId(String providerId);

  Optional<Vehicle> findFirstByLicencePlate(String string);

  List<Vehicle> findByStatusIn(List<Status> stati);

}
