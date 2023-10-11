FROM chonku/java:v0.1.8

ADD target/ping-app-DEV-SNAPSHOT.jar /data/application.jar

COPY ./startup.sh /data/startup.sh

CMD ["/bin/bash", "/data/startup.sh"]

EXPOSE 6060
EXPOSE 6061
