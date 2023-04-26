# Use the official OpenJDK 11 image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the Gradle build files into the container at /app
COPY build.gradle.kts settings.gradle.kts gradlew /app/
COPY gradle gradle

# Copy the source code into the container at /app/src/main/java
COPY src /app/src

# Download and install the Gradle wrapper
RUN ./gradlew wrapper

# Build the application using the Gradle wrapper
RUN ./gradlew bootJar

# Expose port 8085
EXPOSE 8085

# Set the command to run the application when the container starts
CMD ["java", "-jar", "build/libs/WasteServiceGUI-0.0.1-SNAPSHOT.jar"]
