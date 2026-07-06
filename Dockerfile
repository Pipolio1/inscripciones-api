FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/inscripciones-api-0.0.1-SNAPSHOT.jar app.jar
COPY C:/oracle-wallet /app/wallet
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
