package io.laegler.fleet.simulator.service;

import static io.laegler.fleet.model.Status.AVAILABLE;
import static org.apache.lucene.search.join.ScoreMode.Total;
import static org.elasticsearch.common.unit.DistanceUnit.KILOMETERS;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.search.sort.SortBuilders.geoDistanceSort;
import static org.elasticsearch.search.sort.SortOrder.ASC;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import io.laegler.fleet.model.Position;
import io.laegler.fleet.model.Vehicle;

@Repository
@RepositoryRestResource(collectionResourceRel = "vehicles", path = "vehicles")
public class VehicleCustomEsRepository {

  private static final String VEHICLE_PROVIDER_ID = "providerId";
  private static final String VEHICLE_POSITION = "position";
  private static final String VEHICLE_POSITION_LOCATION = "position.location";
  private static final String VEHICLE_STATUS = "status";

  @Autowired
  private ElasticsearchTemplate esTemplate;

  public List<Vehicle> findByCurrentLocationAndRadiusAndAvailableOrderByDistance(Position center,
      double radius) {
    return esTemplate.queryForList(geoQueryBuilder(center).build(), Vehicle.class);
  }

  public Optional<Vehicle> findFirstByCurrentLocationAndRadiusAndAvailableOrderByDistance(
      Position center, double radius) {
    return findByCurrentLocationAndRadiusAndAvailableOrderByDistance(center, radius).stream()
        .findFirst();
  }

  public Optional<Vehicle> findFirstByProviderIdAndCurrentLocationAndRadiusAndAvailableOrderByDistance(
      String providerId, Position center, double radius) {
    return findByProviderIdAndCurrentLocationAndRadiusAndAvailableOrderByDistance(providerId,
        center, radius).stream().findFirst();
  }

  public List<Vehicle> findByProviderIdAndCurrentLocationAndRadiusAndAvailableOrderByDistance(
      String providerId, Position center, double radius) {
    return esTemplate.queryForList(geoQueryBuilder(center)
        .withQuery(boolQuery().must(QueryBuilders.matchQuery(VEHICLE_PROVIDER_ID, providerId)))
        .build(), Vehicle.class);
  }

  private NativeSearchQueryBuilder geoQueryBuilder(Position center) {
    GeoPoint centerPoint = new GeoPoint(center.getLat(), center.getLng());
    return new NativeSearchQueryBuilder()
        .withQuery(nestedQuery(VEHICLE_POSITION,
            boolQuery().filter(geoDistanceQuery(VEHICLE_POSITION_LOCATION).point(centerPoint)
                .distance(100, KILOMETERS)).must(matchAllQuery()),
            Total))
        .withQuery(boolQuery().must(QueryBuilders.matchQuery(VEHICLE_STATUS, AVAILABLE.name())))
        .withSort(
            geoDistanceSort(VEHICLE_POSITION_LOCATION, centerPoint).points(centerPoint).order(ASC));
  }

}
