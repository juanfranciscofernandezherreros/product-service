Feature: Investment funds management (CRUD + NAV + Orders)

  Scenario: Create a valid fund
    When I create a fund with isin "ES0000000001" name "Global" category "RV" currency "EUR" riskLevel 5
    Then the response status is 201
    And the response field "isin" equals "ES0000000001"

  Scenario: Reject a duplicate fund
    Given an existing fund with isin "ES0000000002"
    When I create a fund with isin "ES0000000002" name "Dup" category "RV" currency "EUR" riskLevel 5
    Then the response status is 409
    And the response field "code" equals "RESOURCE_ALREADY_EXISTS"

  Scenario: Reject an invalid fund
    When I create a fund with isin "ES0000000003" name "X" category "RV" currency "EUR" riskLevel 9
    Then the response status is 400

  Scenario: Get fund detail
    Given an existing fund with isin "ES0000000004"
    When I get the fund by isin "ES0000000004"
    Then the response status is 200

  Scenario: Get a missing fund returns 404
    When I get the fund by isin "ZZ0000000000"
    Then the response status is 404
    And the response field "code" equals "RESOURCE_NOT_FOUND"

  Scenario: Update a fund
    Given an existing fund with isin "ES0000000005"
    When I update the fund "ES0000000005" with name "Updated"
    Then the response status is 200
    And the response field "name" equals "Updated"

  Scenario: Delete a fund
    Given an existing fund with isin "ES0000000006"
    When I delete the fund "ES0000000006"
    Then the response status is 204

  Scenario: Delete a missing fund returns 404
    When I delete the fund "ZZ0000000000"
    Then the response status is 404

  Scenario: Cannot delete a fund with active positions
    Given an existing fund with isin "ES0000000007"
    And an active order for fund "ES0000000007"
    When I delete the fund "ES0000000007"
    Then the response status is 422
    And the response field "code" equals "FUND_HAS_ACTIVE_POSITIONS"

  Scenario: List funds filtered by currency
    Given an existing fund with isin "ES0000000008"
    When I list funds with currency "EUR" limit 2 offset 0
    Then the response status is 200
    And the list total is at least 1

  Scenario: Get NAV history
    Given an existing fund with isin "ES0000000009"
    When I get the nav history for "ES0000000009"
    Then the response status is 200
    And the response field "isin" equals "ES0000000009"

  Scenario: Place an order
    Given an existing fund with isin "ES0000000010"
    When I place an order for fund "ES0000000010" amount 1000.00
    Then the response status is 201
    And the response field "status" equals "PENDING"
