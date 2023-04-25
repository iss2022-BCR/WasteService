# Use the official OpenJDK 11 image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy 
COPY unibolibs /unibolibs
# Copy the Gradle build files into the container at /app/
COPY wasteservice_gui/build.gradle wasteservice_gui/settings.gradle wasteservice_gui/gradlew /app/
COPY wasteservice_gui/gradle gradle

# Copy the source code into the container at /app/src/main/java
COPY wasteservice_gui/src /app/src

# Download and install the Gradle wrapper
RUN ./gradlew wrapper

# Build the application using the Gradle wrapper
RUN ./gradlew bootJar

# Expose port 11804
EXPOSE 11804

# Set the command to run the application when the container starts
CMD ["java", "-jar", "build/libs/unibo.wasteservice_gui-0.0.1-SNAPSHOT.jar"]
