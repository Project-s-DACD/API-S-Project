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

    public void start() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.setClientID("xxx");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicName);

        MessageConsumer consumer = session.createDurableSubscriber(topic, "yyy");

        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    String json = ((TextMessage) message).getText();
                    saveEvent(json);
                    System.out.println("Evento recibido y almacenado.");
                } catch (Exception e) {
                    System.err.println("Error al procesar mensaje: " + e.getMessage());
                }
            }
        });

        System.out.println("Suscriptor escuchando el topic: " + topicName);
    }

    private void saveEvent(String json) throws IOException {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        String flightDate = obj.get("flight_date").getAsString();
        String ss = obj.has("ss") ? obj.get("ss").getAsString() : "feederA";
        String datePart = LocalDate.parse(flightDate).format(DateTimeFormatter.BASIC_ISO_DATE);

        File dir = new File(basePath + "/" + topicName + "/" + ss);
        dir.mkdirs();

        File file = new File(dir, datePart + ".events");

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(json + System.lineSeparator());
        }
    }
}
