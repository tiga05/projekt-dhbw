FROM ubuntu:16.04

# install java
RUN apt-get update
RUN apt-get install -y openjdk-8-jre

RUN apt-get install dos2unix

# copy entrypoint
COPY entrypoint.sh /
RUN dos2unix /entrypoint.sh
RUN chmod u+x /entrypoint.sh

# install kafka
ADD http://mirror.23media.de/apache/kafka/0.10.1.0/kafka_2.11-0.10.1.0.tgz /kafka.tgz
RUN tar -xzf /kafka.tgz && mv /kafka_2.11-0.10.1.0 /kafka

# expose kafka port
EXPOSE 2181
EXPOSE 9092

ENTRYPOINT /entrypoint.sh