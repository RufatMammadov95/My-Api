# Welcome to My API

## Task

The task was to build a production-ready flight searching and booking API.

The application manages flight records, provides secure authentication, supports Google OAuth 2.0, uses Redis caching, and exposes flight data through REST and GraphQL.

The API supports flight search, pagination with a maximum page size of 20, user booking operations, and full CRUD operations for flights.

## Description

This project is a backend application built with Java 17, Spring Boot, Spring Security, Spring Data JPA, PostgreSQL, Redis, Swagger, and GraphQL.

The application loads more than 1,100 flight records from a CSV dataset. Flights are the main resource and support full CRUD operations:

```http
GET /api/flights
GET /api/flights/{id}
POST /api/flights
PUT /api/flights/{id}
DELETE /api/flights/{id}
```

Pagination is supported on the flight listing endpoint. The `size` parameter is capped at 20:

```http
GET /api/flights?page=0&size=20
```

Optional origin and destination filtering is also supported:

```http
GET /api/flights?origin=LAX&dest=PHX&page=0&size=20
```

Authentication is handled with Spring Security. Users can create an account and sign in using JWT authentication:

```http
POST /api/auth/signup
POST /api/auth/login
```

After login, use the returned JWT token as a Bearer token:

```http
Authorization: Bearer <token>
```

Booking endpoints require authentication:

```http
POST /api/bookings/{flightId}
GET /api/bookings
PUT /api/bookings/{bookingId}/change-flight/{newFlightId}
DELETE /api/bookings/{bookingId}
```

Google OAuth 2.0 is configured through Spring Security. OAuth credentials are not stored directly in the repository and must be provided through environment variables.

Redis is used for caching. For local development, the project starts an embedded Redis server automatically on port `6379` if Redis is not already running.

The project also provides Swagger UI documentation and a GraphQL interface for flexible flight queries.

## Installation

Make sure Java 17+ is installed.

1. Clone the repository and navigate to the project root directory.
2. Set the required environment variables.
3. Build and run the application.

PowerShell:

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your-postgres-password"
$env:JWT_SECRET="change-this-secret-key-to-at-least-32-characters"
$env:GOOGLE_CLIENT_ID="your-google-client-id"
$env:GOOGLE_CLIENT_SECRET="your-google-client-secret"

.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

For Render deployment, add these environment variables in the Render service settings:

```text
PORT=8080
DB_URL=jdbc:postgresql://<render-postgres-host>:5432/<database-name>
DB_USERNAME=<render-postgres-user>
DB_PASSWORD=<render-postgres-password>
JWT_SECRET=<at-least-32-character-secret>
GOOGLE_CLIENT_ID=<google-client-id>
GOOGLE_CLIENT_SECRET=<google-client-secret>
```

Do not use `localhost` for PostgreSQL on Render. `localhost` means the Render web container itself, not your Render PostgreSQL database.

The local API will be available at:

```text
http://localhost:8080
```

Google OAuth login starts here:

```text
http://localhost:8080/oauth2/authorization/google
```

Google Cloud Console redirect URI:

```text
http://localhost:8080/login/oauth2/code/google
```

## Usage

The application can be tested locally through Swagger UI, Postman, and GraphQL.

Live Production URL:

```text
https://my-api-9vy5.onrender.com
```

Local Base URL:

```text
http://localhost:8080
```

### Swagger UI Documentation

Production Swagger UI:

```text
https://my-api-9vy5.onrender.com/swagger-ui/index.html
```

Local Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

### Postman Collection

The complete API collection is included in this repository:

```text
Travel_API.postman_collection.json
```

Import this file into Postman and run the requests such as Signup, Login, Get Flights with Pagination, Create Flight, Update Flight, Delete Flight, and Booking requests.

Example login body:

```json
{
  "username": "qwasar_student",
  "password": "mysecretpassword"
}
```

### GraphQL Interface

Production GraphQL page:

```text
https://my-api-9vy5.onrender.com/graphiql?path=/graphql
```

Local GraphQL page:

```text
http://localhost:8080/graphiql?path=/graphql
```

GraphQL endpoint:

```http
POST /graphql
```

Example query:

```graphql
{
  allFlights {
    id
    flightNumber
    departureCity
    arrivalCity
    price
  }
}
```

Example query by ID:

```graphql
{
  flightById(id: 1) {
    id
    flightNumber
    departureCity
    arrivalCity
    price
  }
}
```

## Tests

Run the test suite with:

```powershell
.\mvnw.cmd test
```

## The Core Team

Rufat Mammadov - Software Engineer

Made at Qwasar SV -- Software Engineering School

<img alt="Qwasar SV -- Software Engineering School Logo" src="https://storage.googleapis.com/qwasar-public/qwasar-logo_50x50.png" width="20" />
