package io.laegler.fleet.simulator.schedule;

import org.springframework.stereotype.Service;

@Service
public interface NotifyService {

  public static final String JOURNEY = "journey";
  public static final String TRIP = "trip";
  public static final String VEHICLE = "vehicle";
  public static final String PROVIDER = "provider";

  public static final String OPERATION_ADD = "add";
  public static final String OPERATION_UPDATE = "update";
  public static final String OPERATION_DELETE = "delete";

  void notify(String index, String id);

  // void notify(String index, String operation, String id);

}
