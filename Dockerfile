# Build stage
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test --no-daemon

# Run stage
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]