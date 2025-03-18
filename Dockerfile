# Step 1: Use an official Maven image to build the application
FROM maven:3.8.6-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .

# Download dependencies for the project
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src /app/src

# Step 2: Build the application
RUN mvn clean package -DskipTests

# Step 3: Use a lightweight OpenJDK image to run the application
FROM openjdk:17-slim

# Set the working directory for running the app
WORKDIR /app

# Copy the built jar file from the previous stage
COPY --from=build /app/target/secret-santa-generator.jar /app/secret-santa-generator.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/secret-santa-generator.jar"]
