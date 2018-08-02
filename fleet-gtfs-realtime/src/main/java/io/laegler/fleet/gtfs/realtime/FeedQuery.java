package io.laegler.fleet.gtfs.realtime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import org.springframework.validation.annotation.Validated;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationName;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
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
public final class FeedQuery implements Query<QueryData, QueryData, Operation.Variables> {

  // latest(routeId: \"123\", tripId: \"123\", stopId: \"123\")
  public static final String OPERATION_DEFINITION =
      "query {\n" + "  latest {\n" + "    isDeleted\n" + "    tripUpdate {\n" + "      trip {\n"
          + "        tripId\n" + "        routeId\n" + "        directionId\n"
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
      return "FeedQuery";
    }
  };

  private final Operation.Variables variables;

  public FeedQuery() {
    this.variables = Operation.EMPTY_VARIABLES;
  }

  @Override
  public String operationId() {
    return "9462574a955e75e2834f1e11eb5dd620092cef5a9daca89590a78df01d8c088c";
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public QueryData wrapData(QueryData data) {
    return data;
  }

  @Override
  public Operation.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<QueryData> responseFieldMapper() {
    return new QueryData.Mapper();
  }

  @Override
  public OperationName name() {
    return OPERATION_NAME;
  }

}
