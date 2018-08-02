Feature: Check Availability of Taxi Provider

  Scenario: As User I want to get the Availability of the Taxi Provider
    Given I'm logged in as User
    When I check the Availability from "-36.8651021,174.7374951" to "-36.8487779,174.7380663"
    Then the response code should be 200
    And the Taxi Provider should be available

  Scenario: As User I get a negative Availability when Route is out of range
    Given I'm logged in as User
    When I check the Availability from "-66.8651021,74.7374951" to "-66.8487779,74.7380663"
    Then the response code should be 404
    And the Taxi Provider should not be available