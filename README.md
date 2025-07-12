# SCB Assignment - Spring Boot with Java 21

This is a basic Spring Boot application set up with Java 21. It provides a foundation for building Spring-based applications with the latest Java features.

## Prerequisites

- Java 21
- Maven
- Git

## Building the Application

To build the application, run the following command:

```bash
mvn clean install
```

## Running the Application

### Local Development

For local development, use the `local` profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

This will use the configuration in `application-local.properties`, which includes:
- Predefined JWT secret
- Default employee password

### Production

For production environments, run:

```bash
mvn spring-boot:run
```

Make sure to set the required environment variables:
- `JWT_SECRET` - Secret key for JWT token generation
- `DEFAULT_EMPLOYEE_PASSWORD` - Default password for employee accounts

## Application Features

The application provides REST APIs for:
- Authentication and authorization
- Department management
- Employee management

All API endpoints are accessible under the `/api` context path.

## Database

The application uses an H2 in-memory database by default. The H2 console is available at `/api/h2-console` when running with the local profile.

## Docker Support

The application can be built and run using Docker. The Docker setup is available in the `docker` directory.

### Building the Docker Image

To build the Docker image, run the following command from the project root:

```bash
docker build -t scb-assignment -f docker/Dockerfile .
```

### Running the Docker Container

To run the Docker container, use the following command:

```bash
docker run -p 8080:8080 scb-assignment
```

### Using Docker Compose

Alternatively, you can use Docker Compose to build and run the application:

```bash
cd docker
docker-compose up
```

This will build the image and start the container. The application will be available at `http://localhost:8080/api`.
