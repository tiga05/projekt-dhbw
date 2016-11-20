import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicob on 18.11.2016.
 * kafka consumer for spark
 */

public class App {

    private static boolean everythingIsGood = true;

    public static void main(String[] args) throws Exception {
        SparkConf sparkConf = new SparkConf().setAppName("SparkConsumer");

        // Create the streaming context with 60 seconds batch size
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(60000));

        int numThreads = 1;
        Map<String, Integer> topicMap = new HashMap<>();

        //adding topic to topic map
        topicMap.put(Constants.KAFKA_TOPIC, numThreads);

        //string for connection to the kafka server
        String kafkaServer = (Constants.TESTING ? "192.168.99.100:" : "kafka:") + Constants.KAFKA_PORT;

        //kafka stream
        JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jssc, kafkaServer, "abc", topicMap);

        //string to messages
        JavaDStream<KafkaMessage> kafkaMessages = messages.map((Function<Tuple2<String, String>, KafkaMessage>) tuple2 -> JsonConverter.getInstance().getKafkaMessage(tuple2._2()));

        JavaDStream<KafkaMessage> kafkaMessagesStream = kafkaMessages.flatMap((FlatMapFunction<KafkaMessage, KafkaMessage>) x -> Collections.singletonList(x).iterator());

        //map value to boolean
        JavaPairDStream<KafkaMessage, Boolean> messageValues = kafkaMessagesStream.mapToPair((PairFunction<KafkaMessage, KafkaMessage, Boolean>) s -> new Tuple2<>(s, s.getStatus().equals("GOOD")))
                .reduceByKey((Function2<Boolean, Boolean, Boolean>) (b1, b2) -> b1 && b2);

        everythingIsGood = true;

        //foreach rdd
        messageValues.foreachRDD((VoidFunction<JavaPairRDD<KafkaMessage, Boolean>>) kafkaBooleanMessages -> {
            Map<KafkaMessage, Boolean> kafkaMessageBoolean = kafkaBooleanMessages.collectAsMap();
            kafkaMessageBoolean.entrySet().stream().filter(message -> !message.getValue()).forEach(message -> everythingIsGood = false);
        });

        //print if everything is good
        if (everythingIsGood) {
            System.out.println("--------- Everything is good");
        } else {
            System.out.println("--------- Nothing is good");
        }

        kafkaMessagesStream.print();
        messageValues.print();
        jssc.start();
        jssc.awaitTermination();
    }
}
