# Etapa 1: Construcción
<<<<<<< HEAD
FROM maven:3.9.9-eclipse-temurin-17 AS build
=======
FROM maven:3.9.6-eclipse-temurin-17 AS build
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
<<<<<<< HEAD
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/Microservicio-Pedidos-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
=======
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8085
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
ENTRYPOINT ["java", "-jar", "app.jar"]