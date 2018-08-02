package io.laegler.fleet.gtfs.realtime;

import io.swagger.annotations.ApiModel;

@ApiModel(
    description = "The relation between this trip and the static schedule. If a trip is done in accordance with temporary schedule, not reflected in GTFS, then it shouldn't be marked as SCHEDULED, but marked as ADDED.")
public enum TripScheduleRelationship {

  SCHEDULED, ADDED, UNSCHEDULED, CANCELED

}
