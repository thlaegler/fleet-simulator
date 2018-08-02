package io.laegler.fleet.gtfs.realtime;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "The effect of this problem on the affected entity.")
public enum Effect {

  NO_SERVICE, REDUCED_SERVICE, SIGNIFICANT_DELAYS, DETOUR, ADDITIONAL_SERVICE, MODIFIED_SERVICE, OTHER_EFFECT, UNKNOWN_EFFECT, STOP_MOVED

}
