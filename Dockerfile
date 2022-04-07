FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE="target/chess-command-0.0.1-SNAPSHOT.jar"

WORKDIR /opt/chess-command

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","app.jar"]