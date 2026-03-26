# Selenium Java Test Automation Framework

A lightweight, maintainable test automation framework for **Web UI**, **REST API**, and **SOAP API** testing — powered by Cucumber BDD and validated against OpenAPI specs.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Build | Maven |
| Web UI | Selenium 4 + WebDriverManager |
| REST API | Rest Assured 5 |
| SOAP API | Rest Assured (raw XML) |
| OpenAPI Validation | Atlassian swagger-request-validator |
| BDD | Cucumber 7 + JUnit 5 |
| DI | PicoContainer (scenario-scoped) |
| Logging | SLF4J + Logback |

## Project Structure

```
src/
├── main/java/com/framework/
│   ├── config/        ConfigManager — loads config.properties, overridable via -D flags
│   ├── factory/       BrowserFactory — Chrome / Firefox / Edge with headless support
│   └── pages/         Page Objects (BasePage + concrete pages)
│
└── test/
    ├── java/com/framework/
    │   ├── api/           RestClient, SoapClient, OpenApiValidator
    │   ├── context/       TestContext — shared state per scenario (PicoContainer)
    │   ├── stepdefs/      Hooks + Step Definitions (Web, REST, SOAP)
    │   └── runners/       TestRunner (JUnit Platform Suite)
    │
    └── resources/
        ├── features/      Gherkin feature files (web/, rest/, soap/)
        ├── schemas/       OpenAPI spec YAML files
        ├── config.properties
        └── logback-test.xml
```

## Quick Start

```bash
# Run all tests
mvn clean test

# Run only web tests
mvn test -Dcucumber.filter.tags="@web"

# Run only REST API tests
mvn test -Dcucumber.filter.tags="@rest"

# Run only SOAP API tests
mvn test -Dcucumber.filter.tags="@soap"

# Run all API tests
mvn test -Dcucumber.filter.tags="@api"

# Override browser / headless from CLI
mvn test -Dcucumber.filter.tags="@web" -Dbrowser=firefox -Dheadless=true
```

## Configuration

Edit `src/test/resources/config.properties` or override any value via `-D` system properties:

```properties
browser=chrome          # chrome | firefox | edge
headless=true           # run browsers headless
base.url=https://the-internet.herokuapp.com
api.base.url=https://petstore.swagger.io/v2
openapi.spec.path=schemas/petstore-v2.yaml
soap.base.url=http://www.dneonline.com/calculator.asmx
```

## OpenAPI Spec Validation

REST API tests can auto-validate requests **and** responses against an OpenAPI spec. Use the `validated` step variants:

```gherkin
When I send a validated GET request to "/pet/findByStatus?status=available"
```

This uses Atlassian's `swagger-request-validator` — if the response doesn't match the spec, the test fails immediately with a clear diff.

To test against your own API, drop your OpenAPI YAML/JSON into `src/test/resources/schemas/` and update `openapi.spec.path` in config.

## Adding New Tests

1. **Web UI** — Create a Page Object in `pages/`, add steps in `stepdefs/`, write a `.feature` file tagged `@web`
2. **REST API** — Write a `.feature` file tagged `@api @rest`, reuse existing steps or add new ones in `RestApiSteps`
3. **SOAP API** — Write a `.feature` file tagged `@api @soap`, add step definitions in `SoapApiSteps`

## Reports

After a test run, find reports at:
- **HTML**: `target/cucumber-reports/report.html`
- **JSON**: `target/cucumber-reports/report.json`