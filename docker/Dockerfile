
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app


COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src


RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Set environment variables
ENV JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970 \
    DEFAULT_EMPLOYEE_PASSWORD=password


COPY --from=build /app/target/*.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]