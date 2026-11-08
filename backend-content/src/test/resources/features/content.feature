Feature: Content and Blog API

  Background:
    Given the content service is running

  Scenario: Create and fetch an article
    When I create an article with slug "primer-post"
    Then the response status is 201
    And the article slug field is "primer-post"
    When I get the article
    Then the response status is 200

  Scenario: Reject an article with an invalid ruta
    When I create a category with slug "gramatica" and name "Gramatica"
    Then the response status is 201

  Scenario: List articles
    When I create an article with slug "para-listar"
    And I list articles
    Then the response status is 200

  Scenario: Duplicate article returns conflict
    When I create an article with slug "duplicado"
    Then the response status is 201
    When I create an article with slug "duplicado"
    Then the response status is 409

  Scenario: Comment on an article
    When I create an article with slug "comentado"
    And I create a comment on the article with content "Gran articulo"
    Then the response status is 201

  Scenario: Comment on a missing article returns not found
    When I create an article with slug "existe"
    And I delete the article
    Then the response status is 204
    When I create a comment on the article with content "No deberia"
    Then the response status is 404

  Scenario: Create a page
    When I create a page with path "pages/about.html"
    Then the response status is 201

  Scenario: Create a collection
    When I create a collection with slug "coleccion-1" and file "coleccion1.pdf"
    Then the response status is 201

  Scenario: Reject a collection with a non-pdf file
    When I create a collection with slug "coleccion-2" and file "coleccion2.txt"
    Then the response status is 400

  Scenario: Create a downloadable
    When I create a downloadable with slug "apunte-1"
    Then the response status is 201

  Scenario: Create and list movie phrases
    When I create a movie phrase for movie "matrix" number 1
    Then the response status is 201
    When I list movie phrases for movie "matrix"
    Then the response status is 200

  Scenario: Reject a movie phrase with a non-positive number
    When I create a movie phrase for movie "matrix" number 0
    Then the response status is 400
