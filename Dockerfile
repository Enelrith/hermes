FROM amazoncorretto:21
WORKDIR /app
COPY target/*.jar ./app.jar
COPY application.properties ./application.properties
CMD ["java", "-jar", "app.jar"]

