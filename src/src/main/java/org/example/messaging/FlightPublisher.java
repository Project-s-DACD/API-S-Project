package org.example.messaging;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.apiServices.Flight;


import javax.jms.*;

public class FlightPublisher {
    private static final String brokerUrl = "tcp://localhost:61616";
    private static final String topicName = "prediction.Flight";
    private final Gson gson = new Gson();

    public void publish(Flight flight) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        try {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);

            String json = gson.toJson(flight);
            TextMessage message = session.createTextMessage(json);
            producer.send(message);

            System.out.println("Evento publicado: " + json);

            producer.close();
            session.close();
        } finally {
            connection.close();
        }
    }

}
