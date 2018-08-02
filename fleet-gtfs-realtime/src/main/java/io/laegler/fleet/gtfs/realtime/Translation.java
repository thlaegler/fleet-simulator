package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.util.Collections.emptyList;
import java.io.Serializable;
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

@ApiModel(description = "A localized string mapped to a language.")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Translation implements Serializable {

  private static final long serialVersionUID = 1465883721474799390L;

  static final ResponseField[] $responseFields =
      {ResponseField.forString("text", "text", null, false, emptyList()),
          ResponseField.forString("language", "language", null, true, emptyList())};

  @ApiModelProperty("A UTF-8 string containing the message.")
  private @Nonnull String text;

  @ApiModelProperty("BCP-47 language code. Can be omitted if the language is unknown or if no internationalization is done at all for the feed. At most one translation is allowed to have an unspecified language tag - if there is more than one translation, the language must be provided.")
  private String language;

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  /**
   * A UTF-8 string containing the message.
   */
  public @Nonnull String text() {
    return this.text;
  }

  /**
   * BCP-47 language code. Can be omitted if the language is unknown or if no internationalization
   * is done at all for the feed. At most one translation is allowed to have an unspecified language
   * tag - if there is more than one translation, the language must be provided.
   */
  public String language() {
    return this.language;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeString($responseFields[0], text);
        writer.writeString($responseFields[1], language);
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "Translation{" + "text=" + text + ", " + "language=" + language + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Translation) {
      Translation that = (Translation) o;
      return this.text.equals(that.text) && this.language.equals(that.language);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= text.hashCode();
      h *= 1000003;
      h ^= language.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<Translation> {
    @Override
    public Translation map(ResponseReader reader) {
      final String text = reader.readString($responseFields[0]);
      final String language = reader.readString($responseFields[1]);
      return new Translation(text, language);
    }
  }
}
