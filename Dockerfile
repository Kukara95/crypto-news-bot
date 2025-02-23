FROM openjdk:21-jdk-slim AS build
WORKDIR /app

COPY . .

RUN --mount=type=cache,target=/root/.gradle \
	./gradlew dependencies --no-daemon

RUN --mount=type=cache,target=/root/.gradle \
	./gradlew clean build -x test --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/crypto-news-bot-*-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]

