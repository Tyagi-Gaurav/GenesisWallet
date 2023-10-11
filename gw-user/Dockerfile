FROM chonku/java:v0.1.8

ADD target/gw-user-DEV-SNAPSHOT.jar /data/application.jar

COPY ./startup.sh /data/startup.sh

CMD ["/bin/bash", "/data/startup.sh"]

#HEALTHCHECK CMD wget --quiet --tries=1 --spider http://localhost:9090/api/status || exit 1

EXPOSE 9090
EXPOSE 9091
EXPOSE 19090