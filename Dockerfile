FROM java
VOLUME /tmp
ADD erya-0.0.1-SNAPSHOT.jar app.jar
COPY config/ /
RUN bash -c 'touch /app.jar'
EXPOSE 80
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]