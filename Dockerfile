FROM java:8-jdk-alpine
LABEL maintainer=gowtham.t4@gmail.com
VOLUME /tmp
ADD ./target/gfg-catalog-services-0.0.1-SNAPSHOT.war /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
