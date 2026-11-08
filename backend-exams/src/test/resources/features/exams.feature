Feature: Exams management

  Scenario: Create a valid exam
    When I create an exam with codigo "EX-1" titulo "Reading 1"
    Then the status is 201
    And the field "codigo" equals "EX-1"

  Scenario: Reject a duplicate exam code
    Given an existing exam with codigo "EX-2" titulo "Reading 2"
    When I create an exam with codigo "EX-2" titulo "Other"
    Then the status is 409
    And the field "code" equals "RESOURCE_ALREADY_EXISTS"

  Scenario: Get a missing exam returns 404
    When I get the exam with id 999999
    Then the status is 404

  Scenario: Add a question to an exam
    Given an existing exam with codigo "EX-3" titulo "Reading 3"
    When I add a question with orden 1 to the exam
    Then the status is 201

  Scenario: Register a result
    Given an existing exam with codigo "EX-4" titulo "Reading 4"
    When I register a result for user "11111111-1111-1111-1111-111111111111" on the exam
    Then the status is 201

  Scenario: Register a diagnostic
    When I register a diagnostic for user "22222222-2222-2222-2222-222222222222"
    Then the status is 201

  Scenario: Register a simulation
    When I register a simulation for user "33333333-3333-3333-3333-333333333333"
    Then the status is 201
    And the field "modalidad" equals "LISTENING"
