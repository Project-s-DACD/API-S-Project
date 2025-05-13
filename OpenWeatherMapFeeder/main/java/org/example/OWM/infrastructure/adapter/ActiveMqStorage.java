package org.example.OWM.infrastructure.adapter;


import org.example.OWM.domain.LocationWeather;
import org.example.OWM.infrastructure.ports.WeatherStorage;

import javax.jms.*;
import javax.naming.InitialContext;
import com.google.gson.Gson;

public class ActiveMqStorage implements WeatherStorage {
    private final ConnectionFactory factory;
    private final String queueName;
    private final Gson gson = new Gson();

    public ActiveMqStorage(String jndiFactory, String queueName) {
        ConnectionFactory f = null;
        try {
            InitialContext ctx = new InitialContext();
            f = (ConnectionFactory) ctx.lookup(jndiFactory);
        } catch (Exception e) {
            // log.error("Init ActiveMQ failed", e);
        }
        this.factory = f;
        this.queueName = queueName;
    }

    @Override
    public void save(String city, LocationWeather weather) {
        if (factory == null) return;
        try (Connection conn = factory.createConnection();
             Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            Destination queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);
            String json = gson.toJson(weather);
            TextMessage msg = session.createTextMessage(json);
            msg.setStringProperty("city", city);
            producer.send(msg);
        } catch (JMSException e) {
            // log.error("Failed to send weather to queue", e);
        }
    }
}
