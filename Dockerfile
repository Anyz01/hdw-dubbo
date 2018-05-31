FROM blacklabelops/swarm-jdk8
#codenvy/jdk8_maven3_tomcat8

MAINTAINER TuMinglong(tuminglong@126.com)
ENV PATH=$PATH:$JRE_HOME/bin

RUN yum -y install wget tar unzip git \
    && yum -y clean all

EXPOSE 8888 8443
