package io.laegler.fleet.gtfs.realtime;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "The relation between this StopTime and the static schedule.")
public enum StopTimeScheduleRelationship {

  SCHEDULED, SKIPPED, NO_DATA

}
