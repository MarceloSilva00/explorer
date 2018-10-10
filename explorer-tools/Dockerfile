FROM ubuntu:18.04

RUN apt-get update
RUN apt-get install -y make qt4-default g++ tcpdump vim netcat iputils-ping

WORKDIR /etc/explorer-tools

COPY GUISample GUISample

ADD GUISample      GUISample
ADD Labs           Labs
ADD Makefile       Makefile
ADD Viewer         Viewer
ADD jClient        jClient
ADD libRobSock     libRobSock
ADD logplayer      logplayer
ADD pClient        pClient
ADD robsample      robsample
ADD simulator      simulator

RUN make all

ADD startAll       startAll
ADD startSimViewer startSimViewer

CMD ["/bin/bash", "-c", "startSimViewer"]
