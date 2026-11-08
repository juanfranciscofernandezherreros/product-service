Feature: Social and messaging

  Scenario: Create a public profile
    When I create a profile for user "11111111-1111-1111-1111-111111111111" alias "alice_01"
    Then the status is 201
    And the field "alias" equals "alice_01"

  Scenario: Reject a duplicate alias
    Given an existing profile for user "22222222-2222-2222-2222-222222222222" alias "bob_01"
    When I create a profile for user "33333333-3333-3333-3333-333333333333" alias "bob_01"
    Then the status is 409
    And the field "code" equals "RESOURCE_ALREADY_EXISTS"

  Scenario: Reject an invalid alias
    When I create a profile for user "44444444-4444-4444-4444-444444444444" alias "AB"
    Then the status is 400

  Scenario: Get a missing profile returns 404
    When I get the profile of user "99999999-9999-9999-9999-999999999999"
    Then the status is 404

  Scenario: Follow a user
    When user "55555555-5555-5555-5555-555555555555" follows "66666666-6666-6666-6666-666666666666"
    Then the status is 201

  Scenario: Send a message
    When user "77777777-7777-7777-7777-777777777777" sends a message to "88888888-8888-8888-8888-888888888888"
    Then the status is 201

  Scenario: Create a message request
    When user "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa" requests to message "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"
    Then the status is 201
    And the field "estado" equals "PENDIENTE"
