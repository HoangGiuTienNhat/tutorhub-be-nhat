FROM maven:3-openjdk-17 AS build
WORKDIR /app

COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/tutorhub-be-0.0.1-SNAPSHOT.war demo.war
EXPOSE 8080

ENTRYPOINT ["java","-jar","demo.war"]