package data;

/**
 * Created by nicob on 02.11.2016.
 * collection of constant values
 */

@SuppressWarnings("ConstantConditions")
public class Constants {

    //test variable
    public static final boolean TESTING = true;

    //are we on a windows machine
    public static final boolean WINDOWS_MACHINE = System.getProperty("os.name").toLowerCase().matches("(.*)windows(.*)");

    //kafka attributes
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";

    //attributes for the producer
    public static final int KAFKA_PRODUCER_PORT = TESTING ? 1000 : 9092;
    public static final String PRODUCER_BROKER_LIST = "metadata.broker.list";
    public static final String PRODUCER_KAFKA_TOPIC = "messageData";
    public static final String PRODUCER_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String KEY_SERIALIZE = "key.serializer";
    public static final String VALUE_SERIALIZE = "value.serializer";

    //attributes for the consumer
    public static final int KAFKA_CONSUMER_PORT = TESTING ? 1001 : 2181;
    public static final String KAFKA_TOPIC = "prodData";
    public static final String CONNECT_ZOOKEEPER = "zookeeper.connect";
    public static final String GROUP_ID = "group.id";
    public static final String CLIENT_ID = "client.id";
    public static final String KEY_DESERIALIZE = "key.deserializer";
    public static final String VALUE_DESERIALIZE = "value.deserializer";
    public static final String PARTITION = "partition.assignment.strategy";

    //path to erp file
    public static final String FILE_PATH = WINDOWS_MACHINE ? "C:\\Users\\nicob\\dockerDir" : "/Data/";

    //activemq attributes
    public static final int AMQP_PORT = TESTING ? 32774 : 61616;
    public static final String AMQP_TOPIC = "m_orders";

    //mongo-DB attributes
    public static final String MONGO_DB_ADDRESS = TESTING ? "localhost" : "mongodb";
    public static final int MONGO_DB_PORT = TESTING ? 3001 : 27017;
    public static final String MONGO_DB_DATABASE = "meteor";
    public static final String MONGO_DB_COLLECTION_AMQP = "amqp_collection";
    public static final String MONGO_DB_COLLECTION_DIR = "dir_collection";
    public static final String MONGO_DB_COLLECTION_KAFKA = "kafka_collection";

    /**
     * checks the os and determines server address
     */
    public static String getServer() {
        return WINDOWS_MACHINE ? "192.168.99.100" : "127.0.0.1";
    }
}