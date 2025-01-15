# Employee Records Management System

## Description
A comprehensive Employee Records Management System built with Java 17, Spring Boot, and Oracle SQL. The system provides a Swing-based desktop interface for managing employee data with different user roles and permissions.

## Features
- Employee Data Management (CRUD operations)
- Role-based Access Control
- Audit Trail
- Advanced Search and Filtering
- Data Validation
- Reporting Capabilities

## Technical Stack
- Java 17
- Spring Boot
- Oracle SQL Database
- Swing UI with MigLayout
- Docker
- JUnit & Mockito for Testing
- Swagger for API Documentation

## Prerequisites
- JDK 17
- Maven
- Docker
- Oracle Database

## Setup Instructions

### Database Setup
1. Install Oracle Database
2. Create a new database user
3. Update database credentials in `application.properties`

### Building the Application
```bash
mvn clean install
```

### Running with Docker
```bash
docker build -t employee-records-system .
docker run -p 8080:8080 employee-records-system
```

### Running Locally
```bash
mvn spring-boot:run
```

## API Documentation
Access Swagger UI at: http://localhost:8080/swagger-ui/

## Testing
```bash
mvn test
```

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/employee/recordsystem/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       └── ui/
│   └── resources/
└── test/
    └── java/