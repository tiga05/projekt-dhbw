package sender;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import converter.JsonConverter;
import data.Constants;
import messages.ActiveMQMessage;
import messages.DirectoryMessage;
import messages.KafkaMessage;
import messages.Message;
import org.bson.Document;

/**
 * Created by nicob on 02.11.2016.
 * sends data to the mongo-DB
 */

public class DatabaseSender {

    //instance for the singleton pattern
    private static DatabaseSender instance;

    //database for the connection
    private MongoDatabase mongoDatabase;

    private DatabaseSender() {
        MongoClient mongoClient = new MongoClient(Constants.MONGO_DB_ADDRESS, Constants.MONGO_DB_PORT);
        mongoDatabase = mongoClient.getDatabase(Constants.MONGO_DB_DATABASE);
    }

    /**
     *
     * @return the instance of the database sender
     */
    public static DatabaseSender getDatabaseSender() {
        if (instance == null) {
            instance = new DatabaseSender();
        }
        return instance;
    }

    /**
     * inserts a message into the database
     * @param message: message to insert
     */
    public void insertMessage(Message message) {
        String jsonString = JsonConverter.getInstance().toJsonString(message); //convert message to json-String
        Document document = Document.parse(jsonString); //create a document for the database with the json-String

        //depending on the message, insert into one collection
        if (message instanceof ActiveMQMessage) {
            mongoDatabase.getCollection(Constants.MONGO_DB_COLLECTION_AMQP).insertOne(document);
        } else if (message instanceof DirectoryMessage) {
            mongoDatabase.getCollection(Constants.MONGO_DB_COLLECTION_DIR).insertOne(document);
        } else if (message instanceof KafkaMessage) {
            mongoDatabase.getCollection(Constants.MONGO_DB_COLLECTION_KAFKA).insertOne(document);
        }
    }
}