Feature: Courses API

  Background:
    Given the courses service is running

  Scenario: Create a course (active by default)
    When I create a course named "Curso B2 First"
    Then the response status is 201
    And the course active field is true

  Scenario: Get a course
    When I create a course named "Curso para obtener"
    And I get the course
    Then the response status is 200

  Scenario: Delete a course
    When I create a course named "Curso para borrar"
    And I delete the course
    Then the response status is 204

  Scenario: Get a missing course returns not found
    When I create a course named "Curso efimero"
    And I delete the course
    Then the response status is 204
    When I get the course
    Then the response status is 404

  Scenario: Create a unit
    When I create a unit with slug "unidad-1" and level "B2"
    Then the response status is 201

  Scenario: Duplicate unit slug returns conflict
    When I create a unit with slug "unidad-dup" and level "B2"
    Then the response status is 201
    When I create a unit with slug "unidad-dup" and level "C1"
    Then the response status is 409

  Scenario: Create and list activities
    When I create an activity with slug "act-reading-1" and skill "reading"
    Then the response status is 201
    When I list activities
    Then the response status is 200

  Scenario: Reject an activity with an invalid slug
    When I create an activity with slug "AB" and skill "reading"
    Then the response status is 400

  Scenario: Reject an activity with an invalid skill
    When I create an activity with slug "act-bad-skill" and skill "dancing"
    Then the response status is 400

  Scenario: Add a question to an activity
    When I create an activity with slug "act-con-pregunta" and skill "grammar"
    And I add a question with order 1 to the activity
    Then the response status is 201

  Scenario: Duplicate question order returns conflict
    When I create an activity with slug "act-pregunta-dup" and skill "grammar"
    And I add a question with order 1 to the activity
    Then the response status is 201
    When I add a question with order 1 to the activity
    Then the response status is 409

  Scenario: Register an attempt
    When I create an activity with slug "act-con-intento" and skill "listening"
    And I register an attempt on the activity
    Then the response status is 201
