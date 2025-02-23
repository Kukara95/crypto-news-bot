FROM openjdk:21-jdk-slim AS build
WORKDIR /app
COPY . .

RUN ./gradlew clean build -x test

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/crypto-news-bot-*-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]

