package consumer;

import com.google.common.collect.ImmutableMap;
import converter.JsonConverter;
import data.Constants;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import messages.KafkaMessage;

import org.apache.kafka.common.serialization.StringDeserializer;
import sender.DatabaseSender;
import sender.KafkaProducer;
//import stateMachine.FiniteMachine;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nicob on 02.11.2016.
 * class for consuming kafka messages
 */

public class KafkaConsumer implements Runnable {

    //instance for the singleton pattern
    private static KafkaConsumer instance;

    //topic name for the kafka messages and config for the consumer
    private String topicName;
    private ConsumerConfig consumerConfig;

    private KafkaConsumer(String address, String topic) {
        Properties properties = new Properties();
        putProperties(properties, address);
        this.consumerConfig = new ConsumerConfig(properties);
        this.topicName = topic;
    }

    //put properties needed for the kafka consumer
    private void putProperties(Properties properties, String address) {
        properties.put(Constants.BOOTSTRAP_SERVERS, address);
        properties.put(Constants.CONNECT_ZOOKEEPER, address);
        properties.put(Constants.GROUP_ID, "xyz");
        properties.put(Constants.CLIENT_ID,this.getClass().getSimpleName());
        properties.put(Constants.KEY_DESERIALIZE, StringDeserializer.class.getName());
        properties.put(Constants.VALUE_DESERIALIZE, StringDeserializer.class.getName());
        properties.put(Constants.PARTITION, "range");
    }

    /**
     *
     * @param address: address of the kafka server
     * @param topic: topic for the kafka messages
     * @return instance of the kafka consumer
     */
    public static KafkaConsumer getKafkaConsumer(String address, String topic){
        if (instance == null) {
            instance = new KafkaConsumer(address, topic);
        }
        return instance;
    }

    /**
     * receive messages from kafka and convert them for the database
     */
    @Override
    public void run() {
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, List<KafkaStream<byte[], byte[]>>> messages = connector.createMessageStreams(ImmutableMap.of(topicName, 1));
        List<KafkaStream<byte[], byte[]>> messageStreams = messages.get(topicName);
        ExecutorService executorService = Executors.newFixedThreadPool(messageStreams.size());

        for (final KafkaStream<byte[], byte[]> messageStream : messageStreams) {
            executorService.submit(() -> {
                for (MessageAndMetadata<byte[], byte[]> messageAndMetadata : messageStream) {
                    String jsonString = new String(messageAndMetadata.message()); //change messageStream to json-String
                    //convert message
                    KafkaMessage message = JsonConverter.getInstance().getKafkaMessage(jsonString);
                    message.setValue(message.getValue());
                    message.setOrderNumber(AppConsumer.getCurrentOrderNumber());
                    //FiniteMachine.receiveMessage(message);

                    //send message to kafka for analysis with spark
                    KafkaProducer.getInstance().sendMessage(JsonConverter.getInstance().toJsonString(message));

                    if (!Constants.TESTING) {
                        //send message to the database
                        DatabaseSender.getDatabaseSender().insertMessage(message);
                    }
                    System.out.println(message);
                }
            });
        }
    }
}