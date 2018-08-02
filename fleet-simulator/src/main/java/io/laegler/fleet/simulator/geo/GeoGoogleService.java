package io.laegler.fleet.simulator.geo;

import static com.google.maps.model.TrafficModel.BEST_GUESS;
import static com.google.maps.model.TravelMode.DRIVING;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.maps.DirectionsApi;
import com.google.maps.FindPlaceFromTextRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.RoadsApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.FindPlaceFromText;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.SnappedPoint;
import io.laegler.fleet.model.Position;
import io.laegler.fleet.model.Trip;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GeoGoogleService {

  @Autowired
  private GeoApiContext geoApiContext;

  public List<DirectionsRoute> getRoutesByFromAndTo(final String from, final String to) {
    log.trace("Get Routes by from {} to {}", from, to);

    try {
      List<DirectionsResult> results =
          asList(DirectionsApi.getDirections(geoApiContext, from, to).await());
      if (!isEmpty(results)) {
        List<DirectionsRoute> routes = asList(results.get(0).routes);
        routes.forEach(r -> sanitizePolylineForElasticSearch(r));
        return routes;
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot resolve first Waypoint from {} to {}", from, to, ex);
    }
    return emptyList();
  }

  public Optional<DirectionsRoute> getRouteByTrip(@NotNull final Trip trip) {
    log.trace("Get Route by Trip", trip);

    try {
      List<DirectionsResult> results =
          asList(DirectionsApi.newRequest(geoApiContext).trafficModel(BEST_GUESS)
              .departureTime(DateTime.parse(
                  trip.getStart() != null ? trip.getStart().toString() : DateTime.now().toString()))
              .mode(DRIVING).alternatives(false)
              .origin(new LatLng(trip.getFrom().getLat(), trip.getFrom().getLng()))
              .destination(new LatLng(trip.getTo().getLat(), trip.getTo().getLng())).await());
      if (!isEmpty(results)) {
        if (!isEmpty(asList(results.get(0).routes))) {
          DirectionsRoute route = asList(results.get(0).routes).stream().findFirst().get();
          sanitizePolylineForElasticSearch(route);
          return Optional.ofNullable(route);
        }
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot resolve first Waypoint from {} to {}", trip.getFrom(), trip.getTo(), ex);
    }
    return empty();
  }

  public Optional<DirectionsRoute> getRouteFromTo(@NotNull final Position from,
      @NotNull final Position to) {
    log.trace("Get Route by from {} to {}", from, to);

    try {
      List<DirectionsResult> results = asList(DirectionsApi.newRequest(geoApiContext)
          .trafficModel(BEST_GUESS).departureTime(DateTime.now()).mode(DRIVING).alternatives(false)
          .origin(new LatLng(from.getLat(), from.getLng()))
          .destination(new LatLng(to.getLat(), to.getLng())).await());
      if (!isEmpty(results)) {
        if (!isEmpty(asList(results.get(0).routes))) {
          DirectionsRoute route = asList(results.get(0).routes).stream().findFirst().get();
          sanitizePolylineForElasticSearch(route);
          return Optional.ofNullable(route);
        }
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot resolve first Waypoint from {} to {}", from, to, ex);
    }
    return empty();
  }

  public Optional<Position> getPositionByAdress(@NotNull final String address) {
    requireNonNull(address, "address must not be null");
    log.trace("Resolve LatLng by Address: {}", address);

    try {
      List<GeocodingResult> results = asList(GeocodingApi.geocode(geoApiContext, address).await());
      if (!isEmpty(results)) {
        return ofNullable(toPosition(results.get(0).geometry.location));
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot resolve LatLng Address: {}", address, ex);
    }
    return empty();
  }

  public Optional<Position> getPositionByPlaceId(String placeId) {
    return ofNullable(toPosition(getPlaceDetailsByPlaceId(placeId).get(0).geometry.location));
  }

  public List<AutocompletePrediction> getPlaceAutocompleteByAddress(@NotNull final String address) {
    requireNonNull(address, "address must not be null");
    log.trace("Get Places Autocomplete by Address {}", address);

    try {
      List<AutocompletePrediction> results =
          asList(PlacesApi.placeAutocomplete(geoApiContext, address, null).await());
      if (!isEmpty(results)) {
        return results;
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot get  Place Details by Place ID {}", address, ex);
    }
    return emptyList();
  }

  public Optional<String> getAddressByPosition(@NotNull final Position position) {
    requireNonNull(position, "position must not be null");
    log.trace("Get Addess by Position {}", position);

    try {
      List<GeocodingResult> results =
          asList(GeocodingApi.reverseGeocode(geoApiContext, toLatLng(position)).await());
      if (!isEmpty(results)) {
        return ofNullable(results.get(0).formattedAddress);
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot get Addess by Position {}", position, ex);
    }
    return empty();
  }

  public List<PlaceDetails> getPlaceDetailsByPlaceId(@NotNull final String placeId) {
    requireNonNull(placeId, "placeId must not be null");
    log.trace("Get Place Details by Place ID {}", placeId);

    try {
      List<PlaceDetails> results = asList(PlacesApi.placeDetails(geoApiContext, placeId).await());
      if (!isEmpty(results)) {
        return results;
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot get Place Details by Place ID {}", placeId, ex);
    }
    return emptyList();
  }

  public Optional<String> getPlaceIdByAddress(@NotNull final String address) {
    Objects.requireNonNull(address, "address must not be null");
    log.trace("Get Place ID by Address: {}", address);

    try {
      List<FindPlaceFromText> results = asList(PlacesApi
          .findPlaceFromText(geoApiContext, address, FindPlaceFromTextRequest.InputType.TEXT_QUERY)
          .await());
      if (!isEmpty(results)) {
        if (!isEmpty(asList(results.get(0).candidates))) {
          return ofNullable(asList(results.get(0).candidates).get(0).placeId);
        }
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot resolve Place ID Address: {}", address, ex);
    }
    return empty();
  }

  public List<String> getPlaceIdsByAddress(@NotNull final String address) {
    Objects.requireNonNull(address, "address must not be null");
    log.trace("Resolve Place IDs by Address: {}", address);

    try {
      List<FindPlaceFromText> results = asList(PlacesApi
          .findPlaceFromText(geoApiContext, address, FindPlaceFromTextRequest.InputType.TEXT_QUERY)
          .await());
      if (!isEmpty(results)) {
        if (!isEmpty(asList(results.get(0).candidates))) {
          return asList(results.get(0).candidates).stream().map(r -> r.placeId)
              .collect(Collectors.toList());
        }
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot resolve Place IDs Address: {}", address, ex);
    }
    return Collections.emptyList();
  }

  public List<PlacesSearchResult> getNearByPlacesByPosition(@NotNull final Position position) {
    Objects.requireNonNull(position, "LatLng must not be null");
    log.trace("Get nearby Places by LatLng {}", position);

    try {
      List<PlacesSearchResponse> results =
          asList(PlacesApi.nearbySearchQuery(geoApiContext, toLatLng(position)).await());
      if (!isEmpty(results)) {
        if (!isEmpty(asList(results.get(0).results))) {
          return asList(results.get(0).results);
        }
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot get nearby Places by LatLng {}", position, ex);
    }
    return Collections.emptyList();
  }

  public List<Position> getNearestRoadByPosition(final List<Position> positions) {
    Objects.requireNonNull(positions, "Positions must not be null");
    log.trace("Resolve Nearest Road by Positions {}", positions.size());

    try {
      List<LatLng> latLngList = new ArrayList<>();
      for (int i = 0; i < positions.size(); i++) {
        LatLng latLng = new LatLng();
        BeanUtils.copyProperties(positions.get(i), latLng);
        latLngList.add(latLng);
      }
      List<SnappedPoint> results = asList(RoadsApi
          .nearestRoads(geoApiContext, latLngList.toArray(new LatLng[latLngList.size()])).await());
      if (!isEmpty(results)) {
        return results.stream().map(p -> {
          Position position = new Position();
          BeanUtils.copyProperties(p.location, position);
          return position;
        }).collect(toList());
      }
    } catch (ApiException | InterruptedException | IOException ex) {
      log.error("Cannot resolve Nearest Road by LatLngs {}", positions, ex);
    }
    return Collections.emptyList();
  }

  public List<Position> getRandomPosition(@NotNull final String centerAddress, final double circuit,
      final int numberOfTaxis) {
    Objects.requireNonNull(centerAddress, "LatLng must not be null");
    log.trace("Get Random Position on Road by Address {} and Circuit {}", centerAddress, circuit);

    Position centerPosition = getPositionByAdress(centerAddress).orElseThrow(
        () -> new IllegalStateException("Could not resolve city name to geo position"));

    List<Position> taxiPositions = new ArrayList<>();
    Random r = new Random();
    for (int i = 0; i < numberOfTaxis; i++) {
      taxiPositions.add(getRandomPositionInBounds(r, centerPosition, circuit));
    }

    return getNearestRoadByPosition(taxiPositions);
  }

  private Position getRandomPositionInBounds(Random r, final Position center,
      final double circuit) {
    Position position = new Position();

    double latShift = (r.nextDouble() - 0.5) * circuit;
    double lngShift = (r.nextDouble() - 0.5) * circuit;

    position.setLat(center.getLat() + latShift);
    position.setLng(center.getLng() + lngShift);

    return position;
  }

  public LatLng toLatLng(Position position) {
    return new LatLng(position.getLat(), position.getLng());
  }

  public List<LatLng> toLatLngs(List<Position> positions) {
    return positions.stream().map(p -> toLatLng(p)).collect(toList());
  }

  public Position toPosition(LatLng latLng) {
    return Position.builder().lat(latLng.lat).lng(latLng.lng).build();
  }

  /**
   * Workaround because Elasticsearch refuses to digest the google-provided EncodedPolyline which
   * could be useful for drawing the route on a map
   */
  private void sanitizePolylineForElasticSearch(DirectionsRoute route) {
    List<String> lines = new ArrayList<>();
    asList(route.legs).forEach(l -> {
      asList(l.steps).forEach(s -> {
        s.polyline = null;
      });
    });
    route.copyrights = "[{"
        + decodePolyline(route.overviewPolyline.getEncodedPath(), 100000.0).stream()
            .map(e -> "'lat': " + e.getLat() + ", 'lng': " + e.getLng()).collect(joining("},{"))
        + "}]";
    route.overviewPolyline = null;
  }

  public List<Position> decodePolyline(String encoded, double precision) {
    List<Position> track = new ArrayList<Position>();
    int index = 0;
    int lat = 0, lng = 0;

    while (index < encoded.length()) {
      int b, shift = 0, result = 0;
      do {
        b = encoded.charAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
      lat += dlat;

      shift = 0;
      result = 0;
      do {
        b = encoded.charAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
      lng += dlng;

      Position p = Position.builder().lat(lat / precision).lng(lng / precision).build();
      track.add(p);
    }
    return track;
  }

}
