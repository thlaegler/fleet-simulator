package features;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import java.util.regex.Pattern;
import io.laegler.fleet.model.Position;
import io.laegler.fleet.model.Position.PositionBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class World {

  public static String PROTOCOL = "http";
  public static String BASEURL;
  public static RequestSpecification REQ;
  public static Response RESP;

  static {
    String baseUrlFromEnv = System.getenv("BASEURL");
    if (baseUrlFromEnv == null || baseUrlFromEnv.isEmpty()) {
      BASEURL = PROTOCOL + "://localhost:8086";
    } else {
      BASEURL = PROTOCOL + "://" + System.getenv("BASEURL");
    }
    REQ = given().baseUri(BASEURL).contentType(APPLICATION_JSON_UTF8_VALUE).log().all();
  }

  /**
   * Check if the given Position String is a Google Place ID, Address or Lat/Lng
   */
  public static Position getPosition(String positionString) {
    PositionBuilder builder = Position.builder();

    String[] split = positionString.split(",");
    if (split.length == 2) {
      // Lat/Lng
      builder.lat(Double.parseDouble(split[0])).lng(Double.parseDouble(split[1]));
    } else {
      // If upper-case character follows a lower-case character it's most likely a Google Place ID
      if (Pattern.matches(".*[a-z][A-Z].*", positionString)) {
        // Place ID
        builder.placeId(positionString);
      } else {
        // Address String
        builder.address(positionString);
      }
    }

    Position position = builder.build();
    position.setLocation(null);
    return position;
  }

  public static void resetTaxis() {
    REQ.when().put("/taxis/reset");
  }

}
