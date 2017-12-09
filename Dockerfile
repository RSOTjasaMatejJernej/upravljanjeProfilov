FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/upravljanjeProfilov-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "upravljanjeProfilov-1.0.0-SNAPSHOT.jar"]