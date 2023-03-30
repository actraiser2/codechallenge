FROM maven:3.9-eclipse-temurin AS build
COPY . /src/
WORKDIR /src

RUN mvn clean package

FROM eclipse-temurin:17
COPY --from=build /src/target/*.jar /app/codechallenge.jar

EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java", "-jar", ."codechallenge.jar"]

