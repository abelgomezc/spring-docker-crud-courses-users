
ARG MSVC_USUARIOS=mscv-usuarios

FROM openjdk:21-jdk-slim AS builder

#ARG MSVC_USUARIOS=mscv-usuarios
ARG MSVC_USUARIOS
WORKDIR /app/$MSVC_USUARIOS

COPY ./pom.xml /app
COPY ./$MSVC_USUARIOS/.mvn ./.mvn
COPY ./$MSVC_USUARIOS/mvnw .
COPY ./$MSVC_USUARIOS/pom.xml .


RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline

COPY ./$MSVC_USUARIOS/src ./src

RUN ./mvnw clean package -DskipTests
#COPY ./target/mscv-usuarios-0.0.1-SNAPSHOT.jar .
FROM openjdk:21-jdk-slim

WORKDIR /app
RUN mkdir ./logs
#ARG MSVC_USUARIOS=mscv-usuarios
ARG MSVC_USUARIOS
ARG TARGET_FOLDER=/app/$MSVC_USUARIOS/target
COPY --from=builder $TARGET_FOLDER/mscv-usuarios-0.0.1-SNAPSHOT.jar .
ARG PORT_APP=8081
ENV PORT $PORT_APP
EXPOSE $PORT
#ENTRYPOINT ["java", "-jar", "mscv-usuarios-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "mscv-usuarios-0.0.1-SNAPSHOT.jar"]
