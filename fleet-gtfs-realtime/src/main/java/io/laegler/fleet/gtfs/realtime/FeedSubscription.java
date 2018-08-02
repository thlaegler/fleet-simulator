package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import com.apollographql.apollo.api.InputFieldMarshaller;
import com.apollographql.apollo.api.InputFieldWriter;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationName;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.Subscription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@ApiModel(
    description = "A selector for an entity in a GTFS feed. The values of the fields should correspond to the appropriate fields in the GTFS feed. At least one specifier must be given. If several are given, then the matching has to apply to all the given specifiers.")
@JsonInclude(ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@Validated
public final class FeedSubscription
    implements Subscription<SubscriptionData, SubscriptionData, Operation.Variables> {

  // feed(routeId: \"123\", tripId: \"123\", stopId: \"123\")
  public static final String OPERATION_DEFINITION =
      "subscription {\n" + "  feed {\n" + "    isDeleted\n" + "    tripUpdate {\n"
          + "      trip {\n" + "        tripId\n" + "        routeId\n" + "        directionId\n"
          + "        startTime\n" + "        startDate\n" + "        scheduleRelationship\n"
          + "      }\n" + "    }\n" + "    alert {\n" + "      informedEntity {\n"
          + "        agencyId\n" + "        routeId\n" + "        stopId\n" + "        trip {\n"
          + "          tripId\n" + "          routeId\n" + "          directionId\n"
          + "          startTime\n" + "          startDate\n" + "          scheduleRelationship\n"
          + "        }\n" + "        routeType\n" + "      }\n" + "      url {\n"
          + "        translation {\n" + "          text\n" + "          language\n" + "        }\n"
          + "      }\n" + "      headerText {\n" + "        translation {\n" + "          text\n"
          + "          language\n" + "        }\n" + "      }\n" + "      activePeriod {\n"
          + "        start\n" + "        end\n" + "      }\n" + "      effect\n"
          + "      descriptionText {\n" + "        translation {\n" + "          text\n"
          + "          language\n" + "        }\n" + "      }\n" + "    }\n" + "    vehicle {\n"
          + "      occupancyStatus\n" + "      trip {\n" + "        tripId\n" + "        routeId\n"
          + "        startTime\n" + "        startDate\n" + "        directionId\n"
          + "        scheduleRelationship\n" + "      }\n" + "      stopId\n" + "      timestamp\n"
          + "      currentStopSequence\n" + "      congestionLevel\n" + "      vehicle {\n"
          + "        label\n" + "        id\n" + "        licensePlate\n" + "      }\n"
          + "      position {\n" + "        odometer\n" + "        speed\n" + "        longitude\n"
          + "        latitude\n" + "        bearing\n" + "      }\n" + "    }\n" + "  }\n" + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private static final OperationName OPERATION_NAME = new OperationName() {
    @Override
    public String name() {
      return "FeedSubscription";
    }
  };

  private final Operation.Variables variables;

  public FeedSubscription() {
    this.variables = new CustomVariables();
  }

  public FeedSubscription(Map<String, Object> variables) {
    this.variables = new CustomVariables(variables);
  }

  @Override
  public String operationId() {
    return "6b99548f20e2c42c21bf5695582e5d2f46aaa437058dec218c335a1e8c2d2681";
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public SubscriptionData wrapData(SubscriptionData data) {
    return data;
  }

  @Override
  public Operation.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<SubscriptionData> responseFieldMapper() {
    return new SubscriptionData.Mapper();
  }

  @Override
  public OperationName name() {
    return OPERATION_NAME;
  }

  public static class CustomVariables extends Operation.Variables {

    Map<String, Object> variables = new HashMap<>();

    public CustomVariables() {
      super();
    }

    public CustomVariables(Map<String, Object> variables) {
      super();
      this.variables = variables;
    }

    @Override
    @NotNull
    public Map<String, Object> valueMap() {
      return variables;
    }

    @Override
    @NotNull
    public InputFieldMarshaller marshaller() {
      return new InputFieldMarshaller() {
        @Override
        public void marshal(InputFieldWriter writer) {}
      };
    }
  }


}
