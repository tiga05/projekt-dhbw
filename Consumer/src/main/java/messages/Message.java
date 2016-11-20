package messages;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by nicob on 07.11.2016.
 * allows generic transfer for messages
 */

public class Message {
    @JsonProperty("orderNumber")
    private String orderNumber;

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}