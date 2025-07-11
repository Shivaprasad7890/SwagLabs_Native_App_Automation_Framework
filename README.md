# SwagLabs Native mobile app automation (iOS)
====================================================================

Tools and Technologies used
===========================
- Java 17
- Appium 
- TestNG
- Selenium
- Extent Reports
- Log4J2
- IntelliJ IDE
- Maven


Framework features
==================
- Page Object Model design
- Currently it supports iOS mobile app Automation only
- Placeholder has been created for Android app automation


# Important notes:

- Make sure that Appium server is up and running in the terminal before running the scripts
- Starting and Stopping the Appium Server programmatically will be added in future

How to execute test scripts on iOS Simulator
============================================
- Go to 'config.properties' file and update the required iOS Simulator device details
- Approach 1: Open 'testng.xml' file present in the framework (Test class name path is already configured) --> Right click --> Run
- Approach 2: Directly go to 'FirstAssignmentTest.java' --> Right click --> Run
- Approach 3: Go to Maven section (Right side in IntelliJ) --> Click on 'Lifecycle' dropdown --> double click on 'test' life cycle
- Once the execution is completed, test execution report (Extent report) will automatically open in the browser window for the analysis
