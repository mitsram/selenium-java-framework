@web
Feature: Login functionality

  Scenario: Successful login
    Given I open the login page
    When I login with username "tomsmith" and password "SuperSecretPassword!"
    Then I should see a flash message containing "You logged into a secure area!"

  Scenario: Failed login with invalid credentials
    Given I open the login page
    When I login with username "invalid" and password "invalid"
    Then I should see a flash message containing "Your username is invalid!"
