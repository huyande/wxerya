FROM java
VOLUME /tmp
ADD erya-0.0.1-SNAPSHOT.jar app.jar
COPY config/ /
COPY temp/ /temp
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone
RUN bash -c 'touch /app.jar'
EXPOSE 80
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
