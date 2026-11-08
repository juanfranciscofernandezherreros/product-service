Feature: Battle management (questions and rooms)

  Scenario: Create a valid question
    When I create a question level "A1" topic "DAILY" prompt "What time is it?"
    Then the status is 201
    And the field "prompt" equals "What time is it?"

  Scenario: Reject a duplicate question
    Given an existing question level "A2" topic "WORK" prompt "Where do you work?"
    When I create a question level "A2" topic "WORK" prompt "Where do you work?"
    Then the status is 409
    And the field "code" equals "RESOURCE_ALREADY_EXISTS"

  Scenario: Reject an invalid question
    When I create an invalid question
    Then the status is 400

  Scenario: Get a question by id
    Given an existing question level "B1" topic "TRAVEL" prompt "Where are you going?"
    When I get the question by its id
    Then the status is 200

  Scenario: Get a missing question returns 404
    When I get the question with id 999999
    Then the status is 404

  Scenario: Create a room and join it
    When I create a room level "B2" topic "MIXED" duration 180 capacity 4
    Then the status is 201
    When I join the room with alias "player1"
    Then the status is 201
    And the field "alias" equals "player1"
    When I list players of the room
    Then the status is 200
