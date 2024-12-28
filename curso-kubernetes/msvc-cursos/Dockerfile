FROM openjdk:21-jdk-slim AS builder

WORKDIR /app/msvc-cursos

COPY ./pom.xml /app
COPY ./msvc-cursos/.mvn ./.mvn
COPY ./msvc-cursos/mvnw .
COPY ./msvc-cursos/pom.xml .


RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline

COPY ./msvc-cursos/src ./src

RUN ./mvnw clean package -DskipTests
#COPY ./target/mscv-usuarios-0.0.1-SNAPSHOT.jar .
FROM openjdk:21-jdk-slim

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar .

EXPOSE 8082

#ENTRYPOINT ["java", "-jar", "mscv-usuarios-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "msvc-cursos-0.0.1-SNAPSHOT.jar"]
