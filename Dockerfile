
FROM openjdk:17-jdk

LABEL authors="nyaugenya"

ENTRYPOINT ["top", "-b"]

WORKDIR /app

COPY target/procurement-0.0.1-SNAPSHOT.jar-1.0.0.jar /app/procurement.jar

EXPOSE 8081

CMD ["java", "-jar", "procurement.jar"]