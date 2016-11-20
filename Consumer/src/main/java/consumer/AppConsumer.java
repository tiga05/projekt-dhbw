package consumer;

import data.Constants;

/**
 * Created by nicob on 02.11.2016.
 * main class; starts all consumer threads
 */

@SuppressWarnings("ConstantConditions")
public class AppConsumer {

    //global variable to store order number
    private static String currentOrderNumber;

    public static String getCurrentOrderNumber() {
        return currentOrderNumber;
    }

    public static void setCurrentOrderNumber(String curOrderNumber) {
        currentOrderNumber = curOrderNumber;
    }

    public static void main(String[] args) {
        Thread amqpThread = new Thread(ActiveMQConsumer.getActiveMqConsumer(Constants.AMQP_TOPIC, Constants.AMQP_PORT));
        amqpThread.start();

        String kafkaServer = Constants.TESTING ? Constants.getServer() + ":" : "kafka:";
        Thread kafkaThread = new Thread(KafkaConsumer.getKafkaConsumer(kafkaServer + Constants.KAFKA_CONSUMER_PORT, Constants.KAFKA_TOPIC));
        kafkaThread.start();

        Thread directoryThread = new Thread(DirectoryListener.getDirectoryListener(Constants.FILE_PATH));
        directoryThread.start();
    }
}