FROM openjdk:8
#COPY /target/*.jar app.jar
EXPOSE 8080

ARG JAR_FILE=/target/*.jar
ARG FILE_NAME

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "${@}"]
#docker run --name spring-boot-app -d -p 8090:8080 72611cd385af --name=Mohammad --age=23 --salary=someNumer
# similar command to run a sprinbboot app
#java -jar filename.jar --server.port=8090 --name=Mohammad --age=23 --salary=someNumber

