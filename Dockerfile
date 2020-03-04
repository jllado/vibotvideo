FROM adoptopenjdk/openjdk12:x86_64-ubuntu-jre-12.0.2_10
LABEL maintainer="jllado@gmail.com"
RUN apt-get update --fix-missing
RUN apt-get install -y software-properties-common
RUN add-apt-repository ppa:jonathonf/ffmpeg-4
RUN apt-get update
RUN apt-get install -y ffmpeg sox libsox-fmt-mp3
EXPOSE 8080
ADD build/libs/vibotvideo-0.0.1-SNAPSHOT.jar vibotvideo.jar
ADD music music
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/vibotvideo.jar"]