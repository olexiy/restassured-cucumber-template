# RestAssured Cucumber API Testing Template
![API Tests](https://github.com/olexiyy/restassured-cucumber-template/actions/workflows/api-tests.yml/badge.svg)

## Table of Contents
1. [Introduction](#introduction)
2. [Project Structure](#project-structure)
3. [Setup and Installation](#setup-and-installation)
4. [Running Tests](#running-tests)
5. [Writing New Test Cases](#writing-new-test-cases)
6. [Framework Components](#framework-components)
    - [JUnit 5](#junit-5)
    - [RestAssured](#restassured)
    - [Cucumber](#cucumber)
    - [Extent Reports](#extent-reports)
7. [Cheat Sheets](#cheat-sheets)
8. [Best Practices](#best-practices)
9. [Troubleshooting](#troubleshooting)

## Introduction

This project serves as a template for API testing using RestAssured and Cucumber with JUnit 5. It provides a structured approach to writing and executing API tests, with the added benefit of Behavior Driven Development (BDD) using Cucumber.

The template uses:
- Java 11
- Maven for dependency management
- JUnit 5 as the testing framework
- RestAssured for API testing
- Cucumber for BDD-style test writing
- Extent Reports for detailed test reporting

## Project Structure

```
src
├── main
│   └── java
└── test
    ├── java
    │   └── com
    │       └── example
    │           ├── config
    │           │   ├── CucumberSpringConfiguration.java
    │           │   └── TestListener.java
    │           ├── runners
    │           │   └── TestRunner.java
    │           └── stepdefs
    │               └── APIStepDefinitions.java
    └── resources
        ├── features
        │   └── api_tests.feature
        └── log4j2.xml
```

## Setup and Installation

1. Ensure you have Java 11 and Maven installed on your system.
2. Clone this repository:
   ```
   git clone https://github.com/yourusername/restassured-cucumber-template.git
   ```
3. Navigate to the project directory:
   ```
   cd restassured-cucumber-template
   ```
4. Install dependencies:
   ```
   mvn clean install
   ```

## Running Tests

To run the tests, use the following Maven command:

```
mvn clean test
```

After the tests complete, you can find the Extent Report at `target/Spark.html`. Open this file in a web browser to view the detailed test results.

## Writing New Test Cases

1. Create a new feature file in `src/test/resources/features` or add scenarios to existing feature files.
2. Write your scenarios using Gherkin syntax. For example:

   ```gherkin
   Feature: User API Tests

   Scenario: Retrieve user details
     Given the API is available
     When I send a GET request to "/users/1"
     Then the response status code should be 200
     And the response should contain the name "John Doe"
   ```

3. Implement the step definitions in `src/test/java/com/example/stepdefs/APIStepDefinitions.java`:

   ```java
   @When("I send a GET request to {string}")
   public void iSendAGETRequestTo(String endpoint) {
       response = given().baseUri(baseUrl).when().get(endpoint);
   }

   @Then("the response status code should be {int}")
   public void theResponseStatusCodeShouldBe(int statusCode) {
       response.then().statusCode(statusCode);
   }

   @And("the response should contain the name {string}")
   public void theResponseShouldContainTheName(String name) {
       response.then().body("name", equalTo(name));
   }
   ```

4. Run the tests using `mvn clean test`.

## Continuous Integration

This project uses GitHub Actions for continuous integration. On every push to the main branch and every pull request, the tests are automatically run.

### Viewing Test Results

After each GitHub Actions run:

1. Go to the "Actions" tab in the GitHub repository
2. Click on the latest workflow run
3. Scroll down to the "Artifacts" section
4. Download the "test-report" artifact to view the Extent Report locally

Additionally, the latest test report from the main branch is automatically published to GitHub Pages. You can view it online at:

https://{username}.github.io/{repository-name}/Spark.html

Replace `{username}` and `{repository-name}` with your GitHub username and repository name respectively.

The current status of the tests on the main branch is reflected by this badge:


## Framework Components

### JUnit 5

JUnit 5 is used as the underlying test runner. Key features used in this template:

- `@Suite` annotation for test suite configuration
- `@ConfigurationParameter` for setting Cucumber options

### RestAssured

RestAssured is a Java DSL for easy testing of REST services. Key concepts:

- `given()`, `when()`, `then()` syntax for structuring tests
- Request specification and response validation
- JSON path expressions for response parsing

### Cucumber

Cucumber enables Behavior Driven Development. Key components:

- Feature files written in Gherkin syntax
- Step definitions to map Gherkin steps to Java code
- Cucumber expressions for parameter passing

### Extent Reports

Extent Reports provide rich HTML reports for test runs. The `TestListener` class handles report generation.

## Cheat Sheets

### JUnit 5 Annotations
- `@Test`: Denotes a test method
- `@BeforeAll`: Executed before all tests
- `@AfterAll`: Executed after all tests
- `@BeforeEach`: Executed before each test
- `@AfterEach`: Executed after each test

### RestAssured Snippets
```java
// Basic GET request
given().when().get("/api/users").then().statusCode(200);

// POST request with body
given()
    .contentType(ContentType.JSON)
    .body(new User("John", "Doe"))
.when()
    .post("/api/users")
.then()
    .statusCode(201);

// Extracting response data
String name = given().when().get("/api/users/1").then().extract().path("name");
```

### Cucumber Gherkin Syntax
```gherkin
Feature: Feature name

  Scenario: Scenario name
    Given some precondition
    When some action is performed
    Then some result is expected

  Scenario Outline: Test multiple values
    Given a user with name <name>
    When the user logs in
    Then the welcome message should include <name>

    Examples:
      | name  |
      | Alice |
      | Bob   |
```

## Reporting and Performance
### Enriching Reports with Readable Information

To make your Extent Reports more informative and readable, you can add custom logs, attachments, and metadata. Here are some ways to enrich your reports:

1. Add detailed logs:
   In your step definitions, use the `scenario` object to add logs:

   ```java
   @When("I send a GET request to {string}")
   public void iSendAGETRequestTo(String endpoint) {
       scenario.log("Sending GET request to: " + baseUrl + endpoint);
       response = given().baseUri(baseUrl).when().get(endpoint);
       scenario.log("Received response with status code: " + response.getStatusCode());
   }
   ```

2. Attach request and response details:
   You can attach the full request and response details to the report:

   ```java
   @Then("the response status code should be {int}")
   public void theResponseStatusCodeShouldBe(int statusCode) {
       scenario.attach(response.asPrettyString(), "application/json", "API Response");
       response.then().statusCode(statusCode);
   }
   ```

3. Add custom labels or categories:
   Use tags in your feature files to categorize tests:

   ```gherkin
   @get @user
   Scenario: Retrieve user details
     Given the API is available
     When I send a GET request to "/users/1"
     Then the response status code should be 200
   ```

   Then, in your `TestListener`, you can add these tags as labels to your test:

   ```java
   private void scenarioStarted(TestCaseStarted event) {
       String scenarioName = event.getTestCase().getName();
       scenario = extent.createTest(scenarioName);
       event.getTestCase().getTags().forEach(tag -> scenario.assignCategory(tag));
   }
   ```

### Measuring API Performance in Tests

To measure and report on API performance, you can use RestAssured's response time feature along with custom logging. Here's how to implement it:

1. Capture response time in your step definitions:

   ```java
   private long responseTime;

   @When("I send a GET request to {string}")
   public void iSendAGETRequestTo(String endpoint) {
       Response response = given().baseUri(baseUrl).when().get(endpoint);
       responseTime = response.time();
       scenario.log("Request to " + endpoint + " took " + responseTime + " milliseconds");
   }
   ```

2. Add assertions for response time:

   ```java
   @Then("the response time should be less than {int} ms")
   public void theResponseTimeShouldBeLessThan(int expectedTime) {
       assertThat(responseTime, lessThan((long)expectedTime));
       scenario.log("Response time assertion passed: " + responseTime + " ms < " + expectedTime + " ms");
   }
   ```

3. Use these in your feature files:

   ```gherkin
   Scenario: Check user API performance
     Given the API is available
     When I send a GET request to "/users/1"
     Then the response status code should be 200
     And the response time should be less than 1000 ms
   ```

4. For more detailed performance testing, consider using tools like Apache JMeter or Gatling, which can be integrated with your test suite for load testing and detailed performance metrics.

By implementing these techniques, you'll be able to provide more detailed, readable reports and include basic performance metrics in your API tests. This will give you and your team better insights into both the functional correctness and the performance characteristics of your APIs.

## Best Practices

1. Keep scenarios independent and focused on a single behavior.
2. Use background steps for common preconditions.
3. Utilize scenario outlines for data-driven tests.
4. Maintain a clear separation between test logic (step definitions) and test data (feature files).
5. Use tags to categorize and selectively run tests.

## Troubleshooting

- If tests fail to run, ensure all dependencies are correctly installed (`mvn clean install`).
- For "Glue class not found" errors, check the package structure and `TestRunner` configuration.
- If the Extent Report is not generated, verify write permissions in the target directory.

For more help, consult the official documentation for [JUnit 5](https://junit.org/junit5/docs/current/user-guide/), [RestAssured](https://github.com/rest-assured/rest-assured/wiki), [Cucumber](https://cucumber.io/docs/cucumber/), and [Extent Reports](https://www.extentreports.com/docs/versions/4/java/index.html).
