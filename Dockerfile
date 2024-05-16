FROM openjdk:21 as build
WORKDIR /app
COPY . ./
RUN microdnf install findutils
RUN chmod +x gradlew
RUN ./gradlew build -x test

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/build/libs/peak-space-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "peak-space-0.0.1-SNAPSHOT.jar"]
EXPOSE 2024
