Feature: Product management (CRUD)

  Scenario: Create a valid product
    When I create a product with sku "PROD-001" name "Mechanical Keyboard" and price 99.99
    Then the response status is 201
    And the response contains a generated id
    And the response field "sku" equals "PROD-001"

  Scenario: Reject an invalid product
    When I create a product with sku "a" name "" and price 0
    Then the response status is 400

  Scenario: Get a product by id
    Given an existing product with sku "PROD-010" name "Monitor" and price 150.00
    When I get the product by its id
    Then the response status is 200
    And the response field "name" equals "Monitor"

  Scenario: Get a non-existing product returns 404
    When I get a product with a random id
    Then the response status is 404

  Scenario: Update a product
    Given an existing product with sku "PROD-020" name "Old Mouse" and price 20.00
    When I update the product with sku "PROD-021" name "New Mouse" and price 25.50
    Then the response status is 200
    And the response field "name" equals "New Mouse"

  Scenario: Update a non-existing product returns 404
    When I update a random product with sku "PROD-030" name "Ghost" and price 10.00
    Then the response status is 404

  Scenario: Delete a product
    Given an existing product with sku "PROD-040" name "Webcam" and price 45.00
    When I delete the product by its id
    Then the response status is 204
    And getting the product by its id returns 404

  Scenario: Delete a non-existing product returns 404
    When I delete a product with a random id
    Then the response status is 404
