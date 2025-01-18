# Employee Records Management System

## Overview
A comprehensive Employee Records Management System built with Spring Boot and Swing UI. The system allows HR personnel, managers, and administrators to manage employee data efficiently with role-based access control.

## Features
- Complete employee information management
- Role-based access control (HR, Managers, Administrators)
- Advanced search and filtering capabilities
- Comprehensive reporting system
- Audit trailing for all changes
- Modern Swing UI with MigLayout
- Docker support for easy deployment

## Technical Stack
- Java 22
- Spring Boot 3.2.1
- Oracle Database XE 21c
- Hibernate 6.4.1
- Spring Security with JWT
- Swing UI with MigLayout
- JasperReports for reporting
- Docker & Docker Compose
- JUnit 5 & Mockito for testing

## Prerequisites
- JDK 22
- Maven 3.9+
- Docker & Docker Compose
- Oracle Database XE 21c (if running locally)

## Quick Start

### Using Docker (Recommended)
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/employee-records-system.git
   cd employee-records-system
   ```

2. Start the application using Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. Access the application:
   - Swing UI: Run the application jar
   - API Documentation: http://localhost:8080/swagger-ui.html

### Manual Setup
1. Install Oracle Database XE 21c

2. Configure database:
   ```sql
   CREATE USER employee_system IDENTIFIED BY your_password;
   GRANT CONNECT, RESOURCE TO employee_system;
   ```

3. Update application.properties with your database credentials

4. Build the application:
   ```bash
   mvn clean package
   ```

5. Run the application:
   ```bash
   java -jar target/employee-records-system.jar
   ```

## API Documentation
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

## Security
- JWT-based authentication
- Role-based access control:
  - ADMIN: Full system access
  - HR: Employee management
  - MANAGER: Department-specific access

## Testing
- Run unit tests: `mvn test`
- Run integration tests: `mvn verify`
- Postman collection available in `postman/` directory

## Contributing
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.