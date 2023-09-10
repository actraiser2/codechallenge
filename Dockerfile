FROM maven:3.9-eclipse-temurin AS build
WORKDIR /sources
COPY . ./
RUN mvn --batch-mode clean package




FROM eclipse-temurin:17-jre AS extract
WORKDIR /extract
COPY --from=build /sources/target/*.jar codechallenge.jar
RUN java -Djarmode=layertools -jar codechallenge.jar extract


FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=extract /extract/dependencies/ ./
COPY --from=extract /extract/spring-boot-loader/ ./
COPY --from=extract /extract/snapshot-dependencies/ ./
COPY --from=extract /extract/application/ ./


EXPOSE 8080

ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]