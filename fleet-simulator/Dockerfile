ARG BASE_IMAGE
FROM $BASE_IMAGE

ARG SERVICE_NAME

ENV SPRING_PROFILES_ACTIVE default

# Java options environment variable from base image in use
ENV JAVA_OPTS="-Xmx512m -server -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:+UseParallelGC"

EXPOSE 8080

ADD target/${SERVICE_NAME}-service.jar /app.jar

CMD java \
  $JAVA_OPTS \
  -jar \
  -Djava.security.egd=file:/dev/./urandom \
  /app.jar
  