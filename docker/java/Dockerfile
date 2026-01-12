# Use the official OpenJDK base image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY target/interview-prep-0.0.1-SNAPSHOT.jar /app/interview-prep.jar

# Expose the port the Spring Boot app will run on
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "/app/interview-prep.jar"]
