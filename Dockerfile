FROM openjdk:17
EXPOSE 8080
ADD target/reviews-service-queries.jar reviews-service-queries.jar
ENTRYPOINT [ "java", "-jar", "/reviews-service-queries.jar"]