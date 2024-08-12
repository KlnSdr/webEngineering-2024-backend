FROM maven:3.9.7 as maven

WORKDIR /app
COPY . /app

RUN mvn clean package

FROM eclipse-temurin:21
COPY --from=maven /app/Users/service/target/users-service-1.0-SNAPSHOT.jar /app.jar

EXPOSE 13000
ENTRYPOINT ["java","-jar","/app.jar"]
