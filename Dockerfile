FROM java:7
MAINTAINER crawler

# Install maven
RUN apt-get update
RUN apt-get install -y maven

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]
# Adding source, compile and package into a fat jar
RUN ["mvn", "clean dependency:copy-dependencies -DoutputDirectory=target/classes/lib package -X"]

CMD ["java", "-jar", "target/docker_test-0.0.1-SNAPSHOT.jar"]
