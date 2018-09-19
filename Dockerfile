FROM openjdk:10
EXPOSE 8080
ADD target/echokube-1.0.0.jar /echokube.jar
CMD ["java", "-jar", "/echokube.jar"]