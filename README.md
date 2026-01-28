# Hermes

A Spring Boot REST API e-commerce application with JWT authentication, rate limiting, and user management capabilities.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Authentication Flow](#authentication-flow)
- [Testing](#testing)
- [Docker Deployment](#docker-deployment)
- [Configuration Details](#configuration-details)
- [Troubleshooting](#troubleshooting)
- [Contact](#contact)

## Features

- **JWT Authentication**: Secure authentication with access and refresh tokens
- **Role-Based Access Control**: User, Customer, Employee, and Admin roles
- **Rate Limiting**: IP-based rate limiting using Bucket4j
- **User Management**: User registration and customer profiles
- **Database Migration**: Flyway for version-controlled database schema
- **API Documentation**: Interactive Swagger UI documentation
- **Docker Support**: Containerized deployment ready

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.2**
- **Spring Security** with JWT
- **Spring Data JPA**
- **MySQL** database
- **Flyway** for database migrations
- **Bucket4j** for rate limiting
- **MapStruct** for entity-DTO mapping
- **Lombok** for reducing boilerplate
- **SpringDoc OpenAPI** for API documentation
- **Docker** for containerization

## Prerequisites

- Java 21 or higher
- Maven 3.9+
- MySQL 8.0+
- Docker (optional, for containerized deployment)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd hermes
```

### 2. Configure Environment Variables

Copy the example configuration files:

```bash
cp application.properties.example application.properties
cp flyway.conf.example flyway.conf
```

Edit `application.properties` with your database and JWT settings:

```properties
DB_URL=jdbc:mysql://localhost:3306/hermes
DB_USER=your_username
DB_PASSWORD=your_password
DB_SCHEMA=hermes

JWT_SECRET=your-secret-key
JWT_EXPIRATION=1800000

JWT_REFRESH_SECRET=your-refresh-secret-key
JWT_REFRESH_EXPIRATION=604800000
```

Edit `flyway.conf` for database migrations:

```properties
flyway.url=jdbc:mysql://localhost:3306/hermes
flyway.user=your_username
flyway.password=your_password
flyway.defaultSchema=hermes
flyway.cleanDisabled=false
```

## Database Setup

### Create the Database

```sql
CREATE DATABASE hermes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Run Flyway Migrations

```bash
./mvnw flyway:migrate
```

### Initialize Roles

After running migrations, insert the required roles:

```sql
INSERT INTO roles (name) VALUES ('USER'), ('CUSTOMER'), ('EMPLOYEE'), ('ADMIN');
```

## Running the Application

### Development Mode

```bash
./mvnw spring-boot:run
```

### Production Build

```bash
./mvnw clean package
java -jar target/hermes-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Project Structure

```
src/
├── main/
│   ├── java/io/github/enelrith/hermes/
│   │   ├── common/
│   │   │   ├── config/         # OpenAPI configuration
│   │   │   ├── enums/          # Application enums
│   │   │   └── exception/      # Global exception handling
│   │   ├── security/
│   │   │   ├── config/         # Security and web configuration
│   │   │   ├── controller/     # Authentication endpoints
│   │   │   ├── dto/            # Auth request/response DTOs
│   │   │   ├── entity/         # RefreshToken entity
│   │   │   ├── exception/      # Security exceptions
│   │   │   ├── filter/         # JWT authentication filter
│   │   │   └── service/        # Auth, JWT, and rate limit services
│   │   └── user/
│   │       ├── controller/     # User management endpoints
│   │       ├── dto/            # User DTOs
│   │       ├── entity/         # User, Customer, Role, Address entities
│   │       ├── exception/      # User-related exceptions
│   │       ├── mapper/         # MapStruct mappers
│   │       ├── repository/     # JPA repositories
│   │       └── service/        # User business logic
│   └── resources/
│       ├── db/migration/       # Flyway migration scripts
│       └── application.yaml    # Application configuration
└── test/                       # Unit and integration tests
```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| POST | `/auth` | Login user | No |
| POST | `/auth/refresh` | Refresh access token | No |
| POST | `/auth/logout` | Logout user | Yes |

### User Management

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| POST | `/users` | Register new user | No |

### Rate Limiting

- **Auth endpoints**: 5 requests per minute per IP

## Authentication Flow

### 1. Register a User

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "rePassword": "password123"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/auth \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Response:
```json
{
  "accessToken": "your-access-token",
  "expiresIn": 1800000,
  "email": "user@example.com",
  "refreshToken": "your-refresh-token"
}
```

### 3. Access Protected Endpoints

```bash
curl -X GET http://localhost:8080/protected-endpoint \
  -H "Authorization: Bearer <access-token>"
```

### 4. Refresh Token

```bash
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "<refresh-token>"
  }'
```

## Testing

Run all tests:

```bash
./mvnw test
```

Run specific test class:

```bash
./mvnw test -Dtest=AuthServiceTest
```

## Docker Deployment

### Using the Startup Script

The project includes a `startup.sh` script that automates the build and deployment:

```bash
chmod +x startup.sh
./startup.sh
```

This script will:
1. Build the Maven project
2. Stop and remove existing containers
3. Build the Docker image
4. Run the container
5. Wait for the application to be ready

### Manual Docker Commands

Build the project:
```bash
./mvnw clean package
```

Build Docker image:
```bash
docker build -t hermes-app .
```

Run container:
```bash
docker run -d \
  --name hermes \
  -p 8080:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/hermes" \
  hermes-app
```

## Configuration Details

### JWT Token Expiration

- **Access Token**: 30 minutes (1800000 ms)
- **Refresh Token**: 7 days (604800000 ms)

### Security Configuration

- CSRF disabled (stateless REST API)
- Session management: STATELESS
- Password encoding: BCrypt (strength 10)

### Database

- Default schema: `hermes`
- Hibernate DDL: `validate`
- Show SQL: `true`

## Troubleshooting

### Database Connection Issues

- Verify MySQL is running: `mysql -u root -p`
- Check credentials in `application.properties`
- Ensure database `hermes` exists

### Flyway Migration Errors

- Clean and re-migrate: `./mvnw flyway:clean flyway:migrate`
- Check migration scripts in `src/main/resources/db/migration/`

### JWT Token Issues

- Ensure secret keys are at least 256 bits
- Verify token hasn't expired
- Check `Authorization: Bearer <token>` header format

## Roadmap

- [x] Initial Project
- [x] Security, JWT Authentication & Token Rotation
- [x] Role-Based Access Control (RBAC)
- [ ] Product Management And Display to Users
- [ ] Product Ordering System
- [ ] OAuth2 Login
- [ ] Docker Compose Setup for Production

## Contact

- 💼 LinkedIn: [linkedin.com/in/dimitrios-arethas](https://www.linkedin.com/in/dimitrios-arethas-6129bb20a/)
- 📧 Email: arethas.dim@gmail.com
