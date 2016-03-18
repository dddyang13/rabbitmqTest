#!/bin/bash
CLASSPATH="${CLASSPATH}:$JAVA_HOME/lib/tools.jar"
for f in ./lib/*.jar; do
  CLASSPATH="${CLASSPATH}:$f";
done
exec java -cp $CLASSPATH com.crawler.docker_test.RabbitMQMessageReceiver