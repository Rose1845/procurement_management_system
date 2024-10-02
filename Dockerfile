
FROM openjdk:17-alpine

LABEL authors="nyaugenya"

WORKDIR /app

COPY target/procurement-0.0.1-SNAPSHOT.jar  /app/procurement.jar

EXPOSE 8086

CMD ["java", "-jar", "procurement.jar"]

ENTRYPOINT ["java", "-Dspring.profiles.active=k8s", "-jar", "procurement.jar"]
