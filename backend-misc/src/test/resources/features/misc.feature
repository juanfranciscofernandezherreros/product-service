Feature: Misc API (suggestions, logbook, writing drafts)

  Background:
    Given the misc service is running

  Scenario: Create a suggestion
    When I create a suggestion with title "Nueva funcionalidad" and description "Me gustaria poder exportar en PDF"
    Then the response status is 201
    And the response status field is "pendiente"

  Scenario: Reject a suggestion with a short title
    When I create a suggestion with title "abc" and description "Descripcion suficientemente larga"
    Then the response status is 400

  Scenario: Publish a suggestion
    When I create a suggestion with title "Idea publicable" and description "Esta sugerencia se publicara"
    And I publish that suggestion
    Then the response status is 200
    And the response status field is "publicada"

  Scenario: List suggestions
    When I create a suggestion with title "Para listar" and description "Sugerencia para el listado"
    And I list suggestions
    Then the response status is 200

  Scenario: Create a logbook category
    When I create a logbook category named "Gramatica"
    Then the response status is 201

  Scenario: Create a logbook note without category
    When I create a logbook note with content "Hoy aprendi el present perfect"
    Then the response status is 201

  Scenario: Create and list writing drafts
    When I create a writing draft of type "essay"
    Then the response status is 201
    When I list writing drafts
    Then the response status is 200

  Scenario: Reject a writing draft with an invalid type
    When I create a writing draft of type "poem"
    Then the response status is 400
