package features.stepDefinitions;

import static features.World.REQ;
import static features.World.RESP;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import features.World;
import io.laegler.fleet.model.Journey;

public class BookTaxiStepImpl {

  @When("^I book a Taxi from (.*) to (.*)$")
  public void i_book_a_Taxi_from_to(String from, String to) throws Throwable {
    RESP =
        REQ.body(Journey.builder().from(World.getPosition(from)).to(World.getPosition(to)).build())//
            .contentType(JSON).accept(JSON).when().post("/taxis/book");
  }

  @Then("^the Taxi should be booked$")
  public void the_Taxi_should_be_booked() throws Throwable {
    RESP.then().assertThat().body("data.status", equalTo("BOOKED"));
  }

}
