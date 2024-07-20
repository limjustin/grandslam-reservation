FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} grandslam.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "grandslam.jar"]