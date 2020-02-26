FROM azul/zulu-openjdk-alpine:11-jre
VOLUME /tmp
ARG JAVA_OPTS="-noverify -Xmx200m -Xss512k"
ADD target/openwms-gateway.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar
