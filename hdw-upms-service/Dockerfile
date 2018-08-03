# hdw-upms-service docker配置
FROM frolvlad/alpine-oraclejdk8

MAINTAINER TuMinglong (tuminglong@126.com)

VOLUME /tmp

ENV PROJECT_HOME /home/project
ENV TOMCAT_HOME /home/tomcat/webapps
ENV OUTPUT_HOME /output

RUN mkdir -p "$PROJECT_HOME" && mkdir -p "$OUTPUT_HOME" && mkdir -p "$TOMCAT_HOME"

ADD hdw-upms-service.jar /home/project/hdw-upms-service.jar

WORKDIR /home/project

RUN sh -c 'touch  /hdw-upms-service.jar'

ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /home/project/hdw-upms-service.jar" ]

EXPOSE 8181 8182

# docker run -it -d --name hdw-upms-service -p8181:8181 -p8182:8182 -v /output:/output -v /home/tomcat/webapps:/home/tomcat/webapps hdw-upms-service:v1
