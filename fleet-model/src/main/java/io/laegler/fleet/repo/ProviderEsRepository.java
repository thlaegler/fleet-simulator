package io.laegler.fleet.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.model.Provider;

@Repository
@RepositoryRestResource(collectionResourceRel = "providers", path = "providers")
public interface ProviderEsRepository extends ElasticsearchRepository<Provider, String> {

}
