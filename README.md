# SimpleWebserviceTest

A small Java / Spring Boot REST API practice project focused on backend development fundamentals: REST API design, persistence, business validation, centralized error handling, and automated testing.

The project simulates a simple banking use case with accounts and money transfers between accounts.

## Tech Stack

- Java 26
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Maven
- H2 in-memory database
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

- Java 26 or compatible with the configured project version
- Maven installed, or the included Maven Wrapper

Verify your installation with:

```bash
java -version
mvn -version
```

## How to Run the Application

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

## H2 Console

The project uses an in-memory H2 database.

```text
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:accountsdb
Username: sa
Password: <empty>
```

The database is recreated on each application startup.

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

The test suite includes:

- Unit tests for service-level validation
- Integration tests using Spring Boot and MockMvc
- Successful account creation
- Account validation errors
- Successful transfers
- Failed transfers due to insufficient funds
- Missing account scenarios
- Invalid request body handling
- Structured error response validation

## Manual API Testing

The repository includes a `tests.http` file with sample HTTP requests that can be executed from IntelliJ IDEA or another compatible HTTP client.

## Notes

This project is a backend practice project focused on Java, Spring Boot, REST APIs, persistence, transaction handling, error handling, and automated testing.
