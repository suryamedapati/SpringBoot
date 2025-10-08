# Step 1: Use a lightweight JDK base image
FROM eclipse-temurin:17-jdk-alpine

# Step 2: Set a working directory
WORKDIR /app

# Step 3: Copy Maven/Gradle build artifact into container
COPY target/spring-boot-docker.jar app.jar

# Step 4: Expose port
EXPOSE 8080

# Step 5: Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
