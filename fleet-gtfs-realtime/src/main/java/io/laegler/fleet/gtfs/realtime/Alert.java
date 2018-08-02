package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.util.Collections.emptyList;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

@ApiModel(description = "An alert, indicating some sort of incident in the public transit network.")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Alert implements Serializable {

  private static final long serialVersionUID = 3726757439734704693L;

  static final ResponseField[] $responseFields =
      {ResponseField.forList("informedEntity", "informedEntity", null, false, emptyList()),
          ResponseField.forObject("url", "url", null, true, emptyList()),
          ResponseField.forObject("headerText", "headerText", null, true, emptyList()),
          ResponseField.forList("activePeriod", "activePeriod", null, true, emptyList()),
          ResponseField.forString("effect", "effect", null, true, emptyList()),
          ResponseField.forObject("descriptionText", "descriptionText", null, true, emptyList())};

  @ApiModelProperty("Time when the alert should be shown to the user. If missing, the alert will be shown as long as it appears in the feed. If multiple ranges are given, the alert will be shown during all of them.")
  private List<ActivePeriod> activePeriod;

  @ApiModelProperty("Entities whose users we should notify of this alert. At least one informed_entity must be provided.")
  private @Nonnull List<InformedEntity> informedEntity;

  @ApiModelProperty("No Documentation")
  private Cause cause;

  @ApiModelProperty("No Documentation")
  private Effect effect;

  @ApiModelProperty("The URL which provides additional information about the alert.")
  private TranslatedString url;

  @ApiModelProperty("Header for the alert. This plain-text string will be highlighted, for example in boldface.")
  private TranslatedString headerText;

  @ApiModelProperty("Description for the alert. This plain-text string will be formatted as the body of the alert (or shown on an explicit \"expand\" request by the user). The information in the description should add to the information of the header.")
  private TranslatedString descriptionText;

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  public Alert(@Nonnull List<InformedEntity> informedEntity, @Nullable TranslatedString url,
      @Nullable TranslatedString headerText, @Nullable List<ActivePeriod> activePeriod,
      @Nullable Effect effect, @Nullable TranslatedString descriptionText) {
    if (informedEntity == null) {
      throw new NullPointerException("informedEntity can't be null");
    }
    this.informedEntity = informedEntity;
    this.url = url;
    this.headerText = headerText;
    this.activePeriod = activePeriod;
    this.effect = effect;
    this.descriptionText = descriptionText;
  }

  /**
   * Entities whose users we should notify of this alert. At least one informed_entity must be
   * provided.
   */
  public @Nonnull List<InformedEntity> informedEntity() {
    return this.informedEntity;
  }

  /**
   * The URL which provides additional information about the alert.
   */
  public TranslatedString url() {
    return this.url;
  }

  /**
   * Header for the alert. This plain-text string will be highlighted, for example in boldface.
   */
  public TranslatedString headerText() {
    return this.headerText;
  }

  /**
   * Time when the alert should be shown to the user. If missing, the alert will be shown as long as
   * it appears in the feed. If multiple ranges are given, the alert will be shown during all of
   * them.
   */
  public List<ActivePeriod> activePeriod() {
    return this.activePeriod;
  }

  public Effect effect() {
    return this.effect;
  }

  /**
   * Description for the alert. This plain-text string will be formatted as the body of the alert
   * (or shown on an explicit "expand" request by the user). The information in the description
   * should add to the information of the header.
   */
  public TranslatedString descriptionText() {
    return this.descriptionText;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeList($responseFields[0], informedEntity, new ResponseWriter.ListWriter() {
          @Override
          public void write(Object value, ResponseWriter.ListItemWriter listItemWriter) {
            for (InformedEntity $item : informedEntity) {
              listItemWriter.writeObject($item.marshaller());
            }
          }
        });
        writer.writeObject($responseFields[1], url.marshaller());
        writer.writeObject($responseFields[2], headerText.marshaller());
        writer.writeList($responseFields[3], activePeriod,
            activePeriod != null ? new ResponseWriter.ListWriter() {
              @Override
              public void write(Object value, ResponseWriter.ListItemWriter listItemWriter) {
                for (ActivePeriod $item : activePeriod) {
                  listItemWriter.writeObject($item.marshaller());
                }
              }
            } : null);
        writer.writeString($responseFields[4], effect.name());
        writer.writeObject($responseFields[5], descriptionText.marshaller());
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "Alert{" + "informedEntity=" + informedEntity + ", " + "url=" + url + ", "
          + "headerText=" + headerText + ", " + "activePeriod=" + activePeriod + ", " + "effect="
          + effect + ", " + "descriptionText=" + descriptionText + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Alert) {
      Alert that = (Alert) o;
      return this.informedEntity.equals(that.informedEntity) && this.url.equals(that.url)
          && this.headerText.equals(that.headerText) && this.activePeriod.equals(that.activePeriod)
          && this.effect.equals(that.effect) && this.descriptionText.equals(that.descriptionText);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= informedEntity.hashCode();
      h *= 1000003;
      h ^= url.hashCode();
      h *= 1000003;
      h ^= headerText.hashCode();
      h *= 1000003;
      h ^= activePeriod.hashCode();
      h *= 1000003;
      h ^= effect.hashCode();
      h *= 1000003;
      h ^= descriptionText.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<Alert> {
    final InformedEntity.Mapper informedEntityFieldMapper = new InformedEntity.Mapper();

    final TranslatedString.Mapper urlFieldMapper = new TranslatedString.Mapper();

    final TranslatedString.Mapper headerTextFieldMapper = new TranslatedString.Mapper();

    final ActivePeriod.Mapper activePeriodFieldMapper = new ActivePeriod.Mapper();

    final TranslatedString.Mapper descriptionTextFieldMapper = new TranslatedString.Mapper();

    @Override
    public Alert map(ResponseReader reader) {
      final List<InformedEntity> informedEntity =
          reader.readList($responseFields[0], new ResponseReader.ListReader<InformedEntity>() {
            @Override
            public InformedEntity read(ResponseReader.ListItemReader reader) {
              return reader.readObject(new ResponseReader.ObjectReader<InformedEntity>() {
                @Override
                public InformedEntity read(ResponseReader reader) {
                  return informedEntityFieldMapper.map(reader);
                }
              });
            }
          });
      final TranslatedString url = reader.readObject($responseFields[1],
          new ResponseReader.ObjectReader<TranslatedString>() {
            @Override
            public TranslatedString read(ResponseReader reader) {
              return urlFieldMapper.map(reader);
            }
          });
      final TranslatedString headerText = reader.readObject($responseFields[2],
          new ResponseReader.ObjectReader<TranslatedString>() {
            @Override
            public TranslatedString read(ResponseReader reader) {
              return headerTextFieldMapper.map(reader);
            }
          });
      final List<ActivePeriod> activePeriod =
          reader.readList($responseFields[3], new ResponseReader.ListReader<ActivePeriod>() {
            @Override
            public ActivePeriod read(ResponseReader.ListItemReader reader) {
              return reader.readObject(new ResponseReader.ObjectReader<ActivePeriod>() {
                @Override
                public ActivePeriod read(ResponseReader reader) {
                  return activePeriodFieldMapper.map(reader);
                }
              });
            }
          });
      final String effectStr = reader.readString($responseFields[4]);
      final Effect effect;
      if (effectStr != null) {
        effect = Effect.valueOf(effectStr);
      } else {
        effect = null;
      }
      final TranslatedString descriptionText = reader.readObject($responseFields[5],
          new ResponseReader.ObjectReader<TranslatedString>() {
            @Override
            public TranslatedString read(ResponseReader reader) {
              return descriptionTextFieldMapper.map(reader);
            }
          });
      return new Alert(informedEntity, url, headerText, activePeriod, effect, descriptionText);
    }
  }

}
