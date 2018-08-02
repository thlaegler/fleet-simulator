package features.steps;

import static features.World.RESP;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import features.World;

public class CommonStepImpl {

  @Given("^I'm logged in as (.*)$")
  public void im_logged_in_as(String apiClient) {
    World.resetTaxis();
    // REQ.auth().with(httpBasic("username", "password"));
  }

  @Then("^the response code should be (\\d+)$")
  public void the_response_code_should_be(int responseCode) {
    RESP.then().log().all().statusCode(responseCode);
  }

}
