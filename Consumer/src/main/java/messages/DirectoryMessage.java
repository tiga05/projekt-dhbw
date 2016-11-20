package messages;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by nicob on 02.11.2016.
 * message class for erp data from file output
 */

@SuppressWarnings("unused")
public class DirectoryMessage extends Message {
    @JsonProperty("em1")
    private double em1;
    @JsonProperty("em2")
    private double em2;
    @JsonProperty("a1")
    private double a1;
    @JsonProperty("a2")
    private double a2;
    @JsonProperty("b1")
    private double b1;
    @JsonProperty("b2")
    private double b2;
    @JsonProperty("overallStatus")
    private String overallStatus;
    @JsonProperty("ts_start")
    private long ts_start;
    @JsonProperty("ts_stop")
    private long ts_stop;

    @Override
    public String toString() {
        return "Directory-Message: " + "overallStatus: " + overallStatus + "; orderNumber: " + getOrderNumber() + "; em1: " + em1 + "; em2: " + em2 +
                "; a1: " + a1 + "; a2: " + a2 + "; b1: " + b1 + "; b2: " + b2 + "; ts_start: " + ts_start + "; ts_stop: " + ts_stop;
    }
}