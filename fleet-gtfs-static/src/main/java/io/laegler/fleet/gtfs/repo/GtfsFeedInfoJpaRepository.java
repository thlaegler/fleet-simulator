package io.laegler.fleet.gtfs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.gtfs.model.FeedInfo;

@Repository
@RepositoryRestResource(collectionResourceRel = "feed-info", path = "gtfs/feed-info")
public interface GtfsFeedInfoJpaRepository extends JpaRepository<FeedInfo, String> {

}
