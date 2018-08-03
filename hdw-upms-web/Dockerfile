# hdw-upms-web docker配置
FROM frolvlad/alpine-oraclejdk8

MAINTAINER TuMinglong (tuminglong@126.com)

VOLUME /tmp

ENV PROJECT_HOME /home/project
ENV TOMCAT_HOME /home/tomcat/webapps
ENV OUTPUT_HOME /output

RUN mkdir -p "$PROJECT_HOME" && mkdir -p "$OUTPUT_HOME" && mkdir -p "$TOMCAT_HOME"

ADD hdw-upms-web.jar /home/project/hdw-upms-web.jar

WORKDIR /home/project

RUN sh -c 'touch  /hdw-upms-web.jar'

ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /home/project/hdw-upms-web.jar" ]

EXPOSE 8281 8282

# docker run -it -d --name hdw-upms-web -p8281:8281 -p8282:8282 -v /output:/output -v /home/tomcat/webapps:/home/tomcat/webapps hdw-upms-web:v1
