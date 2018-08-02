Feature: Book a Taxi

  Scenario: As User I want to book a Taxi
    Given I'm logged in as User
    When I check the Availability from "ChIJoUPB3bxHDW0RkMiiQ2HvAAU" to "ChIJ3abyGptHDW0REPmiQ2HvAAU"
    And the Taxi Provider is available
    And I book a Taxi from "ChIJoUPB3bxHDW0RkMiiQ2HvAAU" to "ChIJ3abyGptHDW0REPmiQ2HvAAU"
    Then the response code should be 200
    And the Taxi should be booked

  Scenario: As User I cannot book a Taxi when Route is out of range
    Given I'm logged in as User
    When I check the Availability from "ChIJYeZuBI9YwokRjMDs_IEyCwo" to "ChIJK1kKR2lDwokRBXtcbIvRCUE"
    And the Taxi Provider is available
    And I book a Taxi from "ChIJYeZuBI9YwokRjMDs_IEyCwo" to "ChIJK1kKR2lDwokRBXtcbIvRCUE"
    Then the response code should be 404
