package io.laegler.fleet.gtfs.realtime;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Cause of this alert.")
public enum Cause {

  UNKNOWN_CAUSE, OTHER_CAUSE, TECHNICAL_PROBLEM, STRIKE, DEMONSTRATION, ACCIDENT, HOLIDAY, WEATHER, MAINTENANCE, CONSTRUCTION, POLICE_ACTIVITY, MEDICAL_EMERGENCY

}
