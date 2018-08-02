package features.stepDefinitions;

import static features.World.REQ;
import static features.World.RESP;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import features.World;
import io.laegler.fleet.model.Journey;

public class CheckAvailabilityStepImpl {

  @When("^I check the Availability from \"(.*)\" to \"(.*)\"$")
  public void i_check_the_Availability_from_place_id_to_place(String from, String to) {
    RESP =
        REQ.body(Journey.builder().from(World.getPosition(from)).to(World.getPosition(to)).build())
            .contentType(JSON).accept(JSON).when().post("/taxis/availability");
  }

  @When("^the Taxi Provider is available$")
  public void the_Taxi_Provider_is_available() throws Throwable {
    // TODO
  }

  @Then("^the Taxi Provider should be available$")
  public void the_Taxi_Provider_should_be_available() {
    RESP.then().body("data.available", equalTo(true));
  }

  @Then("^the Taxi Provider should not be available$")
  public void the_Taxi_Provider_should_not_be_available() throws Throwable {
    RESP.then().body("data.available", equalTo(false));
  }

}
