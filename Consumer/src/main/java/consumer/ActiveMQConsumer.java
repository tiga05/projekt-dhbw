package consumer;

import converter.XmlConverter;
import data.Constants;

import javax.jms.*;

import messages.ActiveMQMessage;
import org.apache.activemq.*;
import sender.DatabaseSender;
//import stateMachine.FiniteMachine;

/**
 * Created by nicob on 02.11.2016.
 * consumer for activemq messages
 */

@SuppressWarnings("ConstantConditions")
public class ActiveMQConsumer implements Runnable {
    //class variables for session, connection and the topic
    Session session;
    Connection connection;
    String topicName;

    //instance for the singleton pattern
    private static ActiveMQConsumer instance;

    private ActiveMQConsumer(String topicName, int port) {
        this.topicName = topicName;
        String amqpServer = Constants.TESTING ? "tcp://" + Constants.getServer() + ":" + port :
                                "failover:tcp://activemq:" + port;
        //new connection factory with the given server ip and port
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(amqpServer);
        try {
            //create connection
            connection = connectionFactory.createConnection();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param topicName: name of the activeMQ topic
     * @param port: port of the amqp server
     * @return the instance of the consumer
     */
    public static ActiveMQConsumer getActiveMqConsumer(String topicName, int port) {
        if (instance == null) {
            instance = new ActiveMQConsumer(topicName, port);
        }
        return instance;
    }

    /**
     * permanent receiving of activemq messages
     */
    @Override
    public void run() {
        try {
            //start connection
            connection.start();

            //create a session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //determine destination
            Destination destination = session.createTopic(topicName + "?consumer.dispatchAsync= false");

            //create consumer
            MessageConsumer messageConsumer = session.createConsumer(destination);

            //receive and convert message
            //noinspection InfiniteLoopStatement
            while (true) {
                messageConsumer.setMessageListener(message -> {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        String text = "";

                        try {
                            text = textMessage.getText();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }

                        //convert received text to an activemq message
                        ActiveMQMessage mqMessage = XmlConverter.getInstance().getActiveMqMessage(text);
                        String orderNumber = mqMessage.getOrderNumber(); //pick orderNumber as identifying atrribute

                        AppConsumer.setCurrentOrderNumber(orderNumber);
                        //FiniteMachine.createFiniteMachine(orderNumber);

                        if (!Constants.TESTING) {
                            //send message to the database
                            DatabaseSender.getDatabaseSender().insertMessage(mqMessage);
                        }

                        System.out.println(mqMessage);
                    } else {
                        System.out.println(message);
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                //close session and connection
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}