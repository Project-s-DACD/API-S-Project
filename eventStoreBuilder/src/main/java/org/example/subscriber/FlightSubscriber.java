package org.example.subscriber;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FlightSubscriber {

    private static final String brokerUrl = "tcp://localhost:61616";
    private static final String topicName = "prediction.Flight";
    private static final String basePath = "eventstore";

    public void startConnectionToBroker() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.setClientID("event-store-builder");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(topic, "flight-store-subscription");

        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    String json = ((TextMessage) message).getText();
                    saveEventFromTopic(json);
                } catch (EventStoreException | JMSException e) {
                    System.err.println("Error while processing the message: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Subscriber connected to topic: " + topicName);
    }

    private void saveEventFromTopic(String json) throws EventStoreException {
        try {
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

            String ss = obj.has("ss") ? obj.get("ss").getAsString() : "unknown";

            JsonObject data = obj.getAsJsonObject("data");
            String flightDate = data.get("flight_date").getAsString();
            String datePart = LocalDate.parse(flightDate).format(DateTimeFormatter.BASIC_ISO_DATE);

            File dir = new File(basePath + "/" + topicName + "/" + ss);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new EventStoreException("No se pudo crear el directorio para guardar el evento", null);
            }

            File file = new File(dir, datePart + ".events");
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(json + System.lineSeparator());
                System.out.println("Event saved in: " + file.getPath());
            }
        } catch (IOException | NullPointerException e) {
            throw new EventStoreException("Error while processing and saving the event", e);
        }
    }
}
