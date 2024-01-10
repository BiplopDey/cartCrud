# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package

# Stage 2: Create the Docker container
FROM openjdk:17-jdk
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java","-jar","app.jar"]
