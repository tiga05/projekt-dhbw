package converter;

import messages.DirectoryMessage;
import messages.KafkaMessage;
import messages.Message;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by nicob on 02.11.2016.
 * maps json-strings to message objects and backwards
 */

public class JsonConverter {

    //instance for the singleton pattern
    private static JsonConverter instance = new JsonConverter();

    //object mapper for the conversion
    private ObjectMapper mapper = new ObjectMapper();

    /**
     *
     * @return the instance of the json converter
     */
    public static JsonConverter getInstance() {
        return instance;
    }

    private JsonConverter() {
    }

    /**
     *
     * @param jsonString: json string to convert
     * @return the converted kafka message
     */
    public KafkaMessage getKafkaMessage(String jsonString){
        try {
            return mapper.readValue(jsonString, KafkaMessage.class); //convert json-String to kafka message
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param jsonString: json string to convert
     * @return the converted directory message
     */
    public DirectoryMessage getDirectoryMessage(String jsonString){
        try {
            return mapper.readValue(jsonString, DirectoryMessage.class); //convert json-String to directory message
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param message: message to convert
     * @return json string
     */
    public String toJsonString(Message message){
        try {
            return mapper.writeValueAsString(message); //convert message to json-String
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}