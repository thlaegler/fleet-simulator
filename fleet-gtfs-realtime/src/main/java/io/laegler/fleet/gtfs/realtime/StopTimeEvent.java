package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(
    description = "Timing information for a single predicted event (either arrival or departure). Timing consists of delay and/or estimated time, and uncertainty. delay should be used when the prediction is given relative to some existing schedule in GTFS. time should be given whether there is a predicted schedule or not. If both time and delay are specified, time will take precedence (although normally, time, if given for a scheduled trip, should be equal to scheduled time in GTFS + delay). Uncertainty applies equally to both time and delay. The uncertainty roughly specifies the expected error in true delay (but note, we don't yet define its precise statistical meaning). It's possible for the uncertainty to be 0, for example for trains that are driven under computer timing control.")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class StopTimeEvent {

  @ApiModelProperty("Delay (in seconds) can be positive (meaning that the vehicle is late) or negative (meaning that the vehicle is ahead of schedule). Delay of 0 means that the vehicle is exactly on time. Either delay or time must be provided within a StopTimeEvent - both fields cannot be empty.")
  private float delay;

  @ApiModelProperty("Event as absolute time. In POSIX time (i.e., number of seconds since January 1st 1970 00:00:00 UTC). Either delay or time must be provided within a StopTimeEvent - both fields cannot be empty.")
  private float time;

  @ApiModelProperty("If uncertainty is omitted, it is interpreted as unknown. To specify a completely certain prediction, set its uncertainty to 0.")
  private float uncertainty;

}
