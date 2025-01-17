# fuentes: 
# - https://spring.io/guides/gs/spring-boot-docker/
# - https://www.javanicaragua.org/2020/03/29/aplicacion-de-spring-boot-con-mysql-y-docker/
# . https://spring.io/guides/topicals/spring-boot-docker

FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean install -DskipTests
# RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
WORKDIR /workspace/app/target/dependency
RUN jar -xf ../*.jar

# IMAGEN definitiva para correr la aplicación
FROM openjdk:8-jdk-alpine
VOLUME /tmp

# run the app as a non-root user:
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# PUERTO QUE EXPONEMOS
EXPOSE 8080

ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# se añade información para hacer debug
ENTRYPOINT ["java", "-noverify", "-XX:TieredStopAtLevel=1", "-cp","app:app/lib/*","-Dspring.main.lazy-initialization=true","com.montesinos.apikey.manager.RestApiSecuredByHeaderApiKeyKeysManagerApplication"]

# ENTRYPOINT 
# ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", 
# "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]

# pasos para la ejecución
# ...............................................................................................
# 1. construir el jar de la app sin pasar los tests
# ./mvnw clean install -DskipTests

# 2. construir la imagen
# docker build -t fj2m/apikey-manager:0.3 .

# 3. lanzar mysql
# docker start mysql57

# 3.1. crear la red
# docker network create -d bridge apikey-network 
# 3.2. añadir a la network necesaria
# docker network connect apikey-network mysql57

# 4. correr el contenedor
# docker run -d -p 8080:8080 -t --name=apikey-manager --network=apikey-network fj2m/apikey-manager:0.3

# 5. ver los logs
# docker logs CONTAINER ID