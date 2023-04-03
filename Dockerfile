FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
COPY --from=builder /app/target/reviews-service-queries.jar reviews-service-queries.jar
ENTRYPOINT ["java", "-jar", "reviews-service-queries.jar"]