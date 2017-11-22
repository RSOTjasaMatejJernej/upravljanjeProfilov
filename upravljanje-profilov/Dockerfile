FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/jax-rs-2.5.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "jax-rs-2.5.0-SNAPSHOT.jar"]