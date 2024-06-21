# Use the official OpenJDK base image
FROM openjdk:17-jdk-slim

# Set a variable for the app home directory
ARG APP_HOME=/usr/app

# Set the working directory in the container
WORKDIR $APP_HOME

# Copy the built jar file into the image
COPY target/ServerSide-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
