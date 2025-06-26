package org.example.speed;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.OWM.domain.LocationWeather;
import org.example.domain.Flight;
import org.example.serving.DatamartStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.List;

public class BusinessSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(BusinessSubscriber.class);
    private static final String brokerUrl = "tcp://localhost:61616";
    private static final String topicName = "prediction.Flight";

    private final DatamartStore datamart;
    private final Gson gson = new Gson();

    public BusinessSubscriber(DatamartStore datamart) {
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
                    String source = event.get("ss").getAsString();

                    if ("AviationStackFeeder".equals(source)) {
                        JsonObject data = event.getAsJsonObject("data");
                        Flight flight = gson.fromJson(data, Flight.class);
                        if (flight != null && flight.getFlight_date() != null) {
                            datamart.insertFlights(List.of(flight));
                            logger.info("Received and saved flight: {}", flight.getFlight_date());
                        } else {
                            logger.error("Invalid flight: {}", data);
                        }
                    } else if ("feeder-OWM".equals(source)) {
                        LocationWeather weather = gson.fromJson(event, LocationWeather.class);
                        if (weather != null && weather.getCity() != null) {
                            datamart.insertWeather(weather);
                            logger.info("Received and saved weather: {}", weather.getCity());
                        } else {
                            logger.error("Invalid weather: {}", event);
                        }
                    } else {
                        logger.warn("Unknown source '{}', skipping", source);
                    }

                } catch (Exception e) {
                    logger.error("Error while processing the event: {}", e.getMessage(), e);
                }
            }
        });

        logger.info("Subscriber connected to topic: {}", topicName);
    }
}
