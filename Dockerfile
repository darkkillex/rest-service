FROM openjdk:8
EXPOSE 8080
ADD target/rest-service.jar rest-service.jar
ENTRYPOINT ["java","-jar","/rest-service.jar"]
