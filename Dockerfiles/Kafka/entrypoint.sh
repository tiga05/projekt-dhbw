!/bin/bash &

echo "advertised.listeners=PLAINTEXT://kafka:9092" >> /kafka/config/server.properties

# start kafka
/kafka/bin/zookeeper-server-start.sh /kafka/config/zookeeper.properties &
SLEEP 2
/kafka/bin/kafka-server-start.sh /kafka/config/server.properties &

# run endless loop to keep alive
/usr/bin/tail -f /dev/null