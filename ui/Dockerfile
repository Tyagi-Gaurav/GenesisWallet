FROM chonku/java:v0.1.8

ADD target/ui-DEV-SNAPSHOT.jar /data/ui.jar

COPY ./startup.sh /data/startup.sh

CMD ["/bin/bash", "/data/startup.sh"]

#HEALTHCHECK CMD wget --quiet --tries=1 --spider http://localhost:8080/status || exit 1

EXPOSE 8080
EXPOSE 8900
