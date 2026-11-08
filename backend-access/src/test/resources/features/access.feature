Feature: Access management (roles, permissions, assignments)

  Scenario: Create a valid role
    When I create a role "ADMIN" "Administrador"
    Then the status is 201
    And the field "codigo" equals "ADMIN"

  Scenario: Reject a duplicate role
    Given an existing role "EDITOR" "Editor"
    When I create a role "EDITOR" "Editor"
    Then the status is 409
    And the field "code" equals "RESOURCE_ALREADY_EXISTS"

  Scenario: Reject an invalid role
    When I create a role "" ""
    Then the status is 400

  Scenario: Get a role
    Given an existing role "VIEWER" "Viewer"
    When I get the role "VIEWER"
    Then the status is 200

  Scenario: Get a missing role returns 404
    When I get the role "NOPE"
    Then the status is 404

  Scenario: Delete a role
    Given an existing role "TEMP" "Temporal"
    When I delete the role "TEMP"
    Then the status is 204

  Scenario: Assign a permission to a role
    Given an existing role "ADMIN2" "Administrador"
    And an existing permission "READ" "Lectura"
    When I add permission "READ" to role "ADMIN2"
    Then the status is 201
    When I list permissions of role "ADMIN2"
    Then the status is 200

  Scenario: Assign a role to a user
    Given an existing role "MEMBER" "Miembro"
    When I assign role "MEMBER" to user "11111111-1111-1111-1111-111111111111"
    Then the status is 201
    And the field "rol" equals "MEMBER"
