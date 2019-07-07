FROM java:8
VOLUME /tmp
ADD target/spring-cloud-eureka-server01-service.jar /app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8761
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]