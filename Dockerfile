FROM adoptopenjdk/openjdk12:x86_64-ubuntu-jre-12.0.2_10
LABEL maintainer="jllado@gmail.com"
RUN apt-get update
RUN apt-get install -y ffmpeg
EXPOSE 8080
ADD build/libs/vibotvideo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]