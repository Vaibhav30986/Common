
Feature: Login functionality
  I want to use this template for my feature file


  Scenario: Login to freedom application
    Given Open Chrome and start my application
    When I enter valid "LGU" and valid password "welcome1"
    Then I should be able to login
  