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
ADD src /code/src
RUN ["mvn", "package"]

COPY start.sh /code/start.sh
RUN ["chmod","+x","/code/start.sh"]

CMD ["/code/start.sh"]
