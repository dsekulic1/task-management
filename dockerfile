FROM eclipse-temurin:17-jre-alpine

WORKDIR /taskmanagement

COPY target/taskmanagement-0.0.1-SNAPSHOT.jar taskmanagement.jar

EXPOSE 8080

CMD ["java", "-jar", "taskmanagement.jar"]
