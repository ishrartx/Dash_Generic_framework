@MobileTest4
Feature: validating mobile test cases


   @Dashboard_01
  Scenario:Dashboard_validating the login functioanlity

    Given open the app on emulator "Ishrar_11.0.0"
    When user clicks on the login button
    When user clicks on the continue button
    When user grants the permission
     When user enter the mobile number
     When user clicks on continue button
     When user enters the pin number
     And user clicks on "Yes, it’s me" button

