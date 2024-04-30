FROM gradle:latest as build

WORKDIR /app
COPY . ./

RUN gradle clean build -x test

# Stage 2: Final stage
FROM openjdk:21

WORKDIR /app
COPY --from=build /app/target/peakspace-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "peakspace-0.0.1-SNAPSHOT.jar"]
EXPOSE 2023


#FROM openjdk:21 as build
#WORKDIR /app
#COPY . ./
#RUN chmod +x mvnw
#RUN ./mvnw clean package -DskipTests
#
#FROM openjdk:21
#WORKDIR /app
#COPY --from=build /app/target/peakspace-0.0.1-SNAPSHOT.jar .
#CMD ["java", "-jar", "peakspace-0.0.1-SNAPSHOT.jar"]
#EXPOSE 2023