#FROM openjdk:11-jre-slim
FROM openjdk:17-jdk-slim-buster
# Set the working directory in the container
WORKDIR /app
# Copy the built JAR file from the previous stage to the container
COPY ./target/assist-projectlog-0.0.3-SNAPSHOT.jar /app
EXPOSE 8030
# Set the command to run the application
CMD ["java", "-jar", "assist-projectlog-0.0.3-SNAPSHOT.jar"]
