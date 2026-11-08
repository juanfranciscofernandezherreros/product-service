Feature: Financial transactions management

  Scenario: Create a valid transaction
    When I create a transaction with amount 150.50 currency "EUR" type "debit"
    Then the response status is 201
    And the response contains a generated id
    And the response field "status" equals "pending"
    And the response field "type" equals "debit"

  Scenario: Reject an invalid transaction
    When I create a transaction with amount 0 currency "E" type "debit"
    Then the response status is 400
    And the response field "code" equals "INVALID_REQUEST"

  Scenario: Get a transaction by id
    Given an existing transaction with amount 99.00 currency "USD" type "credit"
    When I get the transaction by its id
    Then the response status is 200
    And the response field "currency" equals "USD"

  Scenario: Get a non-existing transaction returns 404
    When I get a transaction with a random id
    Then the response status is 404
    And the response field "code" equals "NOT_FOUND"

  Scenario: List transactions with pagination
    Given 3 existing transactions
    When I list transactions with limit 2 and offset 0
    Then the response status is 200
    And the paged response has total 3 and at most 2 items
