package org.example.infrastructure.messaging;
import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.domain.Flight;
import com.google.gson.JsonObject;


import javax.jms.*;

public class FlightPublisher {
    private static final String brokerUrl = "tcp://localhost:61616";
    private static final String topicName = "prediction.Flight";
    private final Gson gson = new Gson();

    public void publishEventInBroker(Flight flight) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        try {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);

            JsonObject event = new JsonObject();
            event.addProperty("ts", java.time.Instant.now().toString());
            event.addProperty("ss", "feederA");
            event.add("data", gson.toJsonTree(flight));

            String eventString = event.toString();
            TextMessage message = session.createTextMessage(eventString);
            producer.send(message);

            System.out.println("Published event: " + eventString);

            producer.close();
            session.close();
        } catch (RuntimeException e) {
            throw new JMSException("Error building the Json: " + e.getMessage());
        } finally {
            connection.close();
        }
    }



}
