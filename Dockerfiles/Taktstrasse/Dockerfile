FROM ubuntu:16.04

# install java
RUN apt-get update
RUN apt-get install -y openjdk-8-jre
RUN apt-get install dos2unix

# copy entrypoint
COPY entrypoint.sh /
RUN dos2unix /entrypoint.sh
RUN chmod u+x /entrypoint.sh

# copy simulation on docker and set permissions
COPY TaktstrasseOpcServer-0.0.1-SNAPSHOT.jar /
RUN chmod u+x /TaktstrasseOpcServer-0.0.1-SNAPSHOT.jar

# copy Consumer on docker and set permissions
COPY consumer.jar /
RUN chmod u+x /consumer.jar

# create directory for the temporary file
RUN mkdir -p /Data

ENTRYPOINT /entrypoint.sh