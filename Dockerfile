FROM java:8-jdk-alpine

COPY ./target/gfg-catalog-services-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

RUN sh -c 'touch gfg-catalog-services-0.0.1-SNAPSHOT.war'