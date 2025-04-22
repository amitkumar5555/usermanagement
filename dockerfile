#####################################
#   Stage 1 : Build
####################################
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests



#####################################
#   Stage 2 : Run
####################################

# Use a lightweight Java image
FROM eclipse-temurin:17-jre

# set the working directory inside the container
WORKDIR /app

# copy the jar file
COPY --from=builder /app/target/*.jar usermanagement.jar


EXPOSE 7002

# Run the application
CMD [ "java", "-jar", "usermanagement.jar" ]
