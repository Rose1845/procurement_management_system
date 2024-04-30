
FROM openjdk:17-alpine

LABEL authors="nyaugenya"

ENTRYPOINT ["java", "-Dspring.profiles.active=k8s", "-jar", "app.jar"]

WORKDIR /app

COPY target/procurement-0.0.1-SNAPSHOT.jar  /app/procurement.jar

EXPOSE 8081

CMD ["java", "-jar", "procurement.jar"]