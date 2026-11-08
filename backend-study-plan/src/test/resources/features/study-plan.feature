Feature: Study plan management (per user)

  Scenario: Get a missing plan returns 404
    When I get the plan of user "11111111-1111-1111-1111-111111111111"
    Then the status is 404

  Scenario: Upsert and get a plan
    When I upsert the plan of user "22222222-2222-2222-2222-222222222222"
    Then the status is 200
    And the field "minutosDia" equals "30"
    When I get the plan of user "22222222-2222-2222-2222-222222222222"
    Then the status is 200

  Scenario: Upsert an AI plan
    When I upsert the ai-plan of user "33333333-3333-3333-3333-333333333333" with weeks 8
    Then the status is 200
    And the field "semanas" equals "8"

  Scenario: Upsert a goal
    When I upsert the goal of user "44444444-4444-4444-4444-444444444444" tipo "UNIDADES" cantidad 3
    Then the status is 200
    And the field "cantidad" equals "3"

  Scenario: Add a session
    When I add a session of user "55555555-5555-5555-5555-555555555555" tarea "UNIDAD"
    Then the status is 201
