package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.util.Collections.emptyList;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;
import com.apollographql.apollo.api.ResponseField;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseFieldMarshaller;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.ResponseWriter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(
    description = "An internationalized message containing per-language versions of a snippet of text or a URL. One of the strings from a message will be picked up. The resolution proceeds as follows: If the UI language matches the language code of a translation, the first matching translation is picked. If a default UI language (e.g., English) matches the language code of a translation, the first matching translation is picked. If some translation has an unspecified language code, that translation is picked.")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class TranslatedString implements Serializable {

  static final ResponseField[] $responseFields =
      {ResponseField.forList("translation", "translation", null, false, emptyList())};

  @ApiModelProperty("At least one translation must be provided.")
  private @Nonnull List<Translation> translation;

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  /**
   * At least one translation must be provided.
   */
  public @Nonnull List<Translation> translation() {
    return this.translation;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeList($responseFields[0], translation, new ResponseWriter.ListWriter() {
          @Override
          public void write(Object value, ResponseWriter.ListItemWriter listItemWriter) {
            for (Translation $item : translation) {
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
      $toString = "TranslatedString{" + "translation=" + translation + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof TranslatedString) {
      TranslatedString that = (TranslatedString) o;
      return this.translation.equals(that.translation);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= translation.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<TranslatedString> {
    final Translation.Mapper translationFieldMapper = new Translation.Mapper();

    @Override
    public TranslatedString map(ResponseReader reader) {
      final List<Translation> translation =
          reader.readList($responseFields[0], new ResponseReader.ListReader<Translation>() {
            @Override
            public Translation read(ResponseReader.ListItemReader reader) {
              return reader.readObject(new ResponseReader.ObjectReader<Translation>() {
                @Override
                public Translation read(ResponseReader reader) {
                  return translationFieldMapper.map(reader);
                }
              });
            }
          });
      return new TranslatedString(translation);
    }
  }
}
