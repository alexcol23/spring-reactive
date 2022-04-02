FROM openjdk:8
ADD build/libs/learn-reactivespring-0.0.1-SNAPSHOT.jar learn-reactivespring-0.0.1-SNAPSHOT.jar
EXPOSE 8085
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://db:27017/", "-jar", "learn-reactivespring-0.0.1-SNAPSHOT.jar"]