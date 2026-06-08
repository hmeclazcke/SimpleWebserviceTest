# SimpleWebserviceTest

A small Java / Spring Boot REST API practice project focused on backend development fundamentals: REST API design, persistence, business validation, centralized error handling, and automated testing.

The project simulates a simple banking use case with accounts and money transfers between accounts.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Maven
- H2 in-memory database
- PostgreSQL
- Docker Compose
- Testcontainers
- JUnit 5
- MockMvc
- Mockito
- AssertJ

## Main Features

- Account creation and listing
- Money transfers between accounts
- Transfer status tracking (`PENDING`, `COMPLETED`, `FAILED`)
- Business validation for accounts and transfers
- Transaction handling for successful and failed transfers
- Centralized exception handling
- Structured API error responses
- Automated unit and integration tests

## Project Purpose

This project was built as part of a technical refresh process after several years of backend development experience, mainly with Java, Oracle Database, PL/SQL, REST/SOAP integrations, and enterprise applications.

The goal is to practice modern Spring Boot development workflows, REST API design, persistence with JPA, transaction handling, error handling, and automated testing.

This is not intended to be a production-ready banking system.

## Requirements

To run this project locally, you need:

- Java 21 or compatible with the configured project version
- Maven installed, or the included Maven Wrapper
- Docker, if you want to run the application with PostgreSQL

Verify your installation with:

```bash
java -version
mvn -version
```

## How to Run the Application

By default, the application runs with an in-memory H2 database.

From the project root, run:

```bash
mvn spring-boot:run
```

Or using the Maven Wrapper on Windows:

```bash
.\mvnw.cmd spring-boot:run
```

The application starts on the default Spring Boot port:

```text
http://localhost:8080
```

## How to Run with PostgreSQL

The project includes a Docker Compose configuration for running PostgreSQL locally.

Start PostgreSQL:

```bash
docker compose up -d
```

Then run the application with the `dev` profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Or using the Maven Wrapper on Windows:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

The `dev` profile connects to:

```text
JDBC URL: jdbc:postgresql://localhost:5432/simplewebservice
Username: hernan
Password: password
```

To stop PostgreSQL:

```bash
docker compose down
```

To stop PostgreSQL and remove the persisted database volume:

```bash
docker compose down -v
```

## H2 Console

By default, the project uses an in-memory H2 database.

```text
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:accountsdb
Username: sa
Password: <empty>
```

The H2 database is recreated on each application startup.

When running with the `dev` profile, the application uses PostgreSQL instead and the H2 console is disabled.

## API Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/account` | Create an account |
| `GET` | `/api/account` | List accounts |
| `POST` | `/api/transfer` | Create a money transfer |
| `GET` | `/api/transfer` | List transfers |

## Example Account Request

```http
POST /api/account
Content-Type: application/json
```

```json
{
  "holder": "Silvia",
  "balance": 1000.00
}
```

Example response:

```json
{
  "id": 1,
  "holder": "Silvia",
  "balance": 1000.00
}
```

## Example Transfer Request

```http
POST /api/transfer
Content-Type: application/json
```

```json
{
  "originId": 1,
  "destinationId": 2,
  "amount": 100.00
}
```

Example response:

```json
{
  "id": 1,
  "originAccountId": 1,
  "destinationAccountId": 2,
  "amount": 100.00,
  "status": "COMPLETED"
}
```

## Example Error Response

When a business rule fails or a request is invalid, the API returns a structured error response.

Example: missing origin account.

```json
{
  "code": "ACCOUNT_NOT_FOUND",
  "message": "Account not found",
  "details": {
    "accountRole": "ORIGIN"
  }
}
```

Example: insufficient funds.

```json
{
  "code": "INSUFFICIENT_FUNDS",
  "message": "Insufficient funds",
  "details": {
    "accountId": 1
  }
}
```

## How to Run the Tests

From the project root, run:

```bash
mvn test
```

Or using the Maven Wrapper on Windows:

```bash
.\mvnw.cmd test
```

To run the full verification lifecycle, including PostgreSQL integration tests with Testcontainers, run:

```bash
mvn verify
```

Or using the Maven Wrapper on Windows:

```bash
.\mvnw.cmd verify
```

The PostgreSQL integration tests use Testcontainers to start a temporary PostgreSQL Docker container, run the tests, and remove the container afterwards. Docker must be running for these tests.

The test suite includes:

- Unit tests for service-level validation
- Integration tests using Spring Boot and MockMvc
- PostgreSQL integration tests using Testcontainers
- Successful account creation
- Account validation errors
- Successful transfers
- Failed transfers due to insufficient funds
- Missing account scenarios
- Invalid request body handling
- Structured error response validation

## Manual API Testing

The repository includes a `tests.http` file with sample HTTP requests that can be executed from IntelliJ IDEA or another compatible HTTP client.

The same requests can be used when running with either the default H2 database or the PostgreSQL `dev` profile.

## Notes

This project is a backend practice project focused on Java, Spring Boot, REST APIs, persistence, transaction handling, error handling, automated testing, and local database setup with Docker.
