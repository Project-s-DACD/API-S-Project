package org.example.speed;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.serving.DatamartFlightStore;
import org.example.domain.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.List;

public class BusinessSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(BusinessSubscriber.class);
    private static final String brokerUrl = "tcp://localhost:61616";
    private static final String topicName = "prediction.Flight";
    private final DatamartFlightStore datamart;
    private final Gson gson = new Gson();

    public BusinessSubscriber(DatamartFlightStore datamart) {
        this.datamart = datamart;
    }

    public void startRealTimeSubscriber() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.setClientID("business-unit");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(topic, "business-subscription");

        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage textMessage) {
                try {
                    String json = textMessage.getText();
                    JsonObject event = JsonParser.parseString(json).getAsJsonObject();

                    if (!event.has("data") || event.get("data").isJsonNull()) {
                        logger.error("Invalid message: {}", json);
                        return;
                    }

                    JsonObject data = event.getAsJsonObject("data");
                    Flight flight = gson.fromJson(data, Flight.class);

                    if (flight != null && flight.getFlight_date() != null) {
                        datamart.insertFlightsIntoDatabase(List.of(flight));
                        logger.info("Received and saved event: {}", flight.getFlight_date());
                    } else {
                        logger.error("Invalid flights with type null: {}", data);
                    }
                } catch (Exception e) {
                    logger.error("Error while processing the event: {}", e.getMessage(), e);
                }
            }
        });

        logger.info("Subscriber connected to topic: {}", topicName);
    }
}
