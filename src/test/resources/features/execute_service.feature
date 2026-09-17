Feature: Execute Service API

    Scenario: User makes a request to the authenticate endpoint and receives a token
        Given the user makes a request to the authenticate endpoint and receives a token
        When the user makes a request to the mikro v15 endpoint with the token
        Then the response should return 200
