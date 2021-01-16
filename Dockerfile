FROM openjdk:8
#COPY /target/*.jar app.jar
EXPOSE 8080
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

