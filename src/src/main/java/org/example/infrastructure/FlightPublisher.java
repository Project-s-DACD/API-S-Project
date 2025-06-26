package org.example.infrastructure;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.domain.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class FlightPublisher {

    private static final Logger log = LoggerFactory.getLogger(FlightPublisher.class);

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
            event.addProperty("ss", "AviationStackFeeder");
            event.add("data", gson.toJsonTree(flight));

            String eventString = event.toString();
            TextMessage message = session.createTextMessage(eventString);
            producer.send(message);

            log.info("Published event: {}", eventString);

            producer.close();
            session.close();
        } catch (RuntimeException e) {
            throw new JMSException("Error building the Json: " + e.getMessage());
        } finally {
            connection.close();
        }
    }
}
