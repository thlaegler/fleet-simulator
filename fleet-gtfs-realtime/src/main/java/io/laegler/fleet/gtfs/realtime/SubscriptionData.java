package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.util.Collections.emptyList;
import java.io.Serializable;
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
public class SubscriptionData implements Serializable, Operation.Data {

  private static final long serialVersionUID = -8110795485333774110L;

  private static final ResponseField[] $responseFields_bak =
      {ResponseField.forObject("feed", "feed", new UnmodifiableMapBuilder<String, Object>(3)
          .put("routeId", "123").put("stopId", "123").put("tripId", "123").build(), false,
          emptyList())};

  private static final ResponseField[] $responseFields =
      {ResponseField.forObject("feed", "feed", ImmutableMap.of(), false, emptyList())};

  private @Nonnull FeedEntity feed;

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  public @Nonnull FeedEntity feed() {
    return this.feed;
  }

  @Override
  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeObject($responseFields[0], feed.marshaller());
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "Data{" + "feed=" + feed + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof SubscriptionData) {
      SubscriptionData that = (SubscriptionData) o;
      return this.feed.equals(that.feed);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= feed.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<SubscriptionData> {
    final FeedEntity.Mapper feedFieldMapper = new FeedEntity.Mapper();

    @Override
    public SubscriptionData map(ResponseReader reader) {
      final FeedEntity feed =
          reader.readObject($responseFields[0], new ResponseReader.ObjectReader<FeedEntity>() {
            @Override
            public FeedEntity read(ResponseReader reader) {
              return feedFieldMapper.map(reader);
            }
          });
      return SubscriptionData.builder().feed(feed).build();
    }
  }

}
