
FROM openjdk:17-jdk

LABEL authors="nyaugenya"

ENTRYPOINT ["top", "-b"]

WORKDIR /app

COPY target/springdemo-1.0.0.jar /app/springdemo.jar

EXPOSE 8081

CMD ["java", "-jar", "procurement.jar"]