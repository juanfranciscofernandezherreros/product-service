Feature: Vocabulary management (items + progress)

  Scenario: Create a valid item
    When I create an item with en "book" es "libro" level "A1"
    Then the status is 201
    And the field "en" equals "book"

  Scenario: Reject a duplicate item
    Given an existing item with en "house" es "casa" level "A2"
    When I create an item with en "house" es "casa" level "A2"
    Then the status is 409
    And the field "code" equals "RESOURCE_ALREADY_EXISTS"

  Scenario: Reject an invalid item
    When I create an item with en "" es "" level "A1"
    Then the status is 400

  Scenario: Get an item by id
    Given an existing item with en "car" es "coche" level "B1"
    When I get the item by its id
    Then the status is 200
    And the field "es" equals "coche"

  Scenario: Get a missing item returns 404
    When I get the item with id 999999
    Then the status is 404

  Scenario: Update an item
    Given an existing item with en "dog" es "perro" level "A1"
    When I update the item with es "canino" level "B2"
    Then the status is 200
    And the field "es" equals "canino"

  Scenario: Delete an item
    Given an existing item with en "cat" es "gato" level "A1"
    When I delete the item by its id
    Then the status is 204

  Scenario: List items filtered by level
    Given an existing item with en "tree" es "arbol" level "B2"
    When I list items with level "B2"
    Then the status is 200
    And the list total is at least 1

  Scenario: Upsert progress for a user
    Given an existing item with en "sun" es "sol" level "A1"
    When I upsert progress for user "11111111-1111-1111-1111-111111111111" with lastGrade 1 streak 2
    Then the status is 200
    And the field "streak" equals "2"
