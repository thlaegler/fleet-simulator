package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.util.Collections.emptyList;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ResponseField;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseFieldMarshaller;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.ResponseWriter;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@ApiModel
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class QueryData implements Serializable, Operation.Data {

  private static final long serialVersionUID = -8110795485333774110L;

  static final ResponseField[] $responseFields_bak =
      {ResponseField
          .forList(
              "latest", "latest", new UnmodifiableMapBuilder<String, Object>(3)
                  .put("routeId", "123").put("stopId", "123").put("tripId", "123").build(),
              false, emptyList())};

  static final ResponseField[] $responseFields =
      {ResponseField.forList("latest", "latest", ImmutableMap.of(), false, emptyList())};

  private @Nonnull List<FeedEntity> latest;

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  public @Nonnull List<FeedEntity> latest() {
    return this.latest;
  }

  @Override
  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeList($responseFields[0], latest, new ResponseWriter.ListWriter() {
          @Override
          public void write(Object value, ResponseWriter.ListItemWriter listItemWriter) {
            for (FeedEntity $item : latest) {
              listItemWriter.writeObject($item.marshaller());
            }
          }
        });
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "Data{" + "latest=" + latest + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof QueryData) {
      QueryData that = (QueryData) o;
      return this.latest.equals(that.latest);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= latest.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<QueryData> {
    final FeedEntity.Mapper latestFieldMapper = new FeedEntity.Mapper();

    @Override
    public QueryData map(ResponseReader reader) {
      final List<FeedEntity> latest =
          reader.readList($responseFields[0], new ResponseReader.ListReader<FeedEntity>() {
            @Override
            public FeedEntity read(ResponseReader.ListItemReader reader) {
              return reader.readObject(new ResponseReader.ObjectReader<FeedEntity>() {
                @Override
                public FeedEntity read(ResponseReader reader) {
                  return latestFieldMapper.map(reader);
                }
              });
            }
          });
      return QueryData.builder().latest(latest).build();
    }
  }

}
