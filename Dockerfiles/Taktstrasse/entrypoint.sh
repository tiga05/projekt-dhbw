#!/bin/bash

# start consumer
java -jar consumer.jar &

# start simulation
java -jar TaktstrasseOpcServer-0.0.1-SNAPSHOT.jar -o /Data -amqp tcp://activemq:61616 -d 15000 -kafka kafka:9092 -topic prodData &

# run endless loop to keep alive
/usr/bin/tail -f /dev/null