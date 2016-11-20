package messages;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by nicob on 02.11.2016.
 * message class for production line messages
 */

@SuppressWarnings("unused")
public class KafkaMessage extends Message {
    @JsonProperty("value")
    private Object value;
    @JsonProperty("booleanValue")
    private boolean booleanValue;
    @JsonProperty("intValue")
    private int intValue;
    @JsonProperty("doubleValue")
    private double doubleValue;
    @JsonProperty("status")
    private String status;
    @JsonProperty("itemName")
    private String itemName;
    @JsonProperty("timestamp")
    private long timeStamp;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;

        String stringValue = value.toString(); //get value as String
        if (stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("false")) { //set boolean value
            booleanValue = Boolean.valueOf(stringValue);
        } else if (stringValue.contains(".") || stringValue.contains(",")){ //set double value
            doubleValue = Double.valueOf(stringValue);
        } else { //set integer value
            intValue = Integer.valueOf(stringValue);
        }
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    @Override
    public String toString() {
        return "Kafka-Message: " + itemName + "; value: " + value + "; booleanValue: " + booleanValue + "; intValue: " + intValue +
                "; doubleValue: " + doubleValue + "; status: " + status + "; orderNumber: " + getOrderNumber() + "; timeStamp: " + timeStamp;
    }
}