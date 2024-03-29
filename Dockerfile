FROM maven:3.9-eclipse-temurin-17 AS build
LABEL maintainer="jmbesada.juez@gmail.com"

COPY . /sources
WORKDIR /sources
RUN mvn --batch-mode -DskipTests=true clean package


FROM eclipse-temurin:17 AS extract
WORKDIR /extract
COPY --from=build /sources/target/*.jar codechallenge.jar
RUN java -Djarmode=layertools -jar codechallenge.jar extract


FROM eclipse-temurin:17
WORKDIR /app
COPY --from=extract /extract/dependencies/ ./
COPY --from=extract /extract/spring-boot-loader/ ./
COPY --from=extract /extract/snapshot-dependencies/ ./
COPY --from=extract /extract/application/ ./


EXPOSE 8080

ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]