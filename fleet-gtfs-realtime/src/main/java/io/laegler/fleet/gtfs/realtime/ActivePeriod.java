package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.util.Collections.emptyList;
import java.io.Serializable;
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
    description = "  A time interval. The interval is considered active at time t if t is greater than or equal to the start time and less than the end time.")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ActivePeriod implements Serializable {

  private static final long serialVersionUID = 8568787460238192183L;

  private static final ResponseField[] $responseFields =
      {ResponseField.forDouble("start", "start", null, true, emptyList()),
          ResponseField.forDouble("end", "end", null, true, emptyList())};

  @ApiModelProperty("Start time, in POSIX time (i.e., number of seconds since January 1st 1970 00:00:00 UTC). If missing, the interval starts at minus infinity. If a TimeRange is provided, either start or end must be provided - both fields cannot be empty.")
  private Double start;

  @ApiModelProperty("End time, in POSIX time (i.e., number of seconds since January 1st 1970 00:00:00 UTC). If missing, the interval ends at plus infinity. If a TimeRange is provided, either start or end must be provided - both fields cannot be empty.")
  private Double end;

  private volatile String $toString;

  private volatile int $hashCode;

  private volatile boolean $hashCodeMemoized;

  /**
   * Start time, in POSIX time (i.e., number of seconds since January 1st 1970 00:00:00 UTC). If
   * missing, the interval starts at minus infinity. If a TimeRange is provided, either start or end
   * must be provided - both fields cannot be empty.
   */
  public Double start() {
    return this.start;
  }

  /**
   * End time, in POSIX time (i.e., number of seconds since January 1st 1970 00:00:00 UTC). If
   * missing, the interval ends at plus infinity. If a TimeRange is provided, either start or end
   * must be provided - both fields cannot be empty.
   */
  public Double end() {
    return this.end;
  }

  public ResponseFieldMarshaller marshaller() {
    return new ResponseFieldMarshaller() {
      @Override
      public void marshal(ResponseWriter writer) {
        writer.writeDouble($responseFields[0], start);
        writer.writeDouble($responseFields[1], end);
      }
    };
  }

  @Override
  public String toString() {
    if ($toString == null) {
      $toString = "ActivePeriod{" + "start=" + start + ", " + "end=" + end + "}";
    }
    return $toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ActivePeriod) {
      ActivePeriod that = (ActivePeriod) o;
      return this.start.equals(that.start) && this.end.equals(that.end);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!$hashCodeMemoized) {
      int h = 1;
      h *= 1000003;
      h ^= start.hashCode();
      h *= 1000003;
      h ^= end.hashCode();
      $hashCode = h;
      $hashCodeMemoized = true;
    }
    return $hashCode;
  }

  public static final class Mapper implements ResponseFieldMapper<ActivePeriod> {
    @Override
    public ActivePeriod map(ResponseReader reader) {
      final Double start = reader.readDouble($responseFields[0]);
      final Double end = reader.readDouble($responseFields[1]);
      return new ActivePeriod(start, end);
    }
  }
}
