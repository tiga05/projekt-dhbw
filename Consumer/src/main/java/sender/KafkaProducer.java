package sender;

import data.Constants;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by nicob on 13.11.2016.
 * write messages to kafka
 */

@SuppressWarnings("ConstantConditions")
public class KafkaProducer {

    //instance for the singleton pattern
    private static KafkaProducer instance;

    //producer needed to send message
    private Producer<String, String> producer;

    /**
     *
     * @return return the instance of the kafka producer
     */
    public static KafkaProducer getInstance() {
        if (instance == null) {
            instance = new KafkaProducer();
        }
        return instance;
    }

    private KafkaProducer() {
        Properties properties = new Properties();
        putProperties(properties);
        producer = new org.apache.kafka.clients.producer.KafkaProducer<>(properties);
    }

    //put properties needed for the kafka consumer
    private void putProperties(Properties properties) {
        String kafkaString = (Constants.TESTING ? Constants.getServer() + ":" : "kafka:") + Constants.KAFKA_PRODUCER_PORT;
        properties.put(Constants.BOOTSTRAP_SERVERS, kafkaString);
        properties.put(Constants.PRODUCER_BROKER_LIST, kafkaString);
        properties.put(Constants.KEY_SERIALIZE, Constants.PRODUCER_SERIALIZER);
        properties.put(Constants.VALUE_SERIALIZE, Constants.PRODUCER_SERIALIZER);
    }

    /**
     *
     * @param data: string to send
     */
    public void sendMessage(String data) {
        producer.send(new ProducerRecord<>(Constants.PRODUCER_KAFKA_TOPIC, data));
    }
}