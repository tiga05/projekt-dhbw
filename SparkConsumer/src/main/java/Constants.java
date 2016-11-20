/**
 * Created by nicob on 18.11.2016.
 * collection of constant values
 */

@SuppressWarnings("ConstantConditions")
public class Constants {

    public static final boolean TESTING = true;

    //kafka attributes
    public static final int KAFKA_PORT = TESTING ? 1001 : 2181;
    public static final String KAFKA_TOPIC = "messageData";
}