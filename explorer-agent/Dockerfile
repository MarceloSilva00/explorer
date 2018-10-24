FROM ubuntu:18.04

RUN apt-get update
RUN apt-get install -y openjdk-8-jre-headless make qt4-default g++ tcpdump vim netcat iputils-ping

WORKDIR /etc/explorer-sample

ADD target/explorer-agent-0.1.0.jar explorer-agent.jar


CMD ["java", "-jar", "explorer-agent.jar", "-host", "localhost:6000", "-pos", "1", "-robname", "explorer-agent"]
