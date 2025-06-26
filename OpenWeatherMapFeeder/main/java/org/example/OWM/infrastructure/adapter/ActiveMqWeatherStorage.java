package org.example.OWM.infrastructure.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.example.OWM.domain.LocationWeather;
import org.example.OWM.infrastructure.ports.WeatherStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.time.Instant;

public class ActiveMqWeatherStorage implements WeatherStorage {
    private static final Logger log = LoggerFactory.getLogger(ActiveMqWeatherStorage.class);

    private final Gson gson;
    private final ActiveMqManager manager;
    private static final String TOPIC_NAME = "Weather";

    public ActiveMqWeatherStorage(String brokerUrl) {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.toString()))
                .create();
        this.manager = new ActiveMqManager(brokerUrl);
    }

    @Override
    public void save(LocationWeather weather) {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = manager.createConnection();
            session = manager.createSession(connection);
            // Publicar en topic en lugar de cola
            Topic topic = session.createTopic(TOPIC_NAME);
            producer = session.createProducer(topic);

            String payload = gson.toJson(weather);
            TextMessage message = session.createTextMessage(payload);
            producer.send(message);
            log.info("Published weather event to topic {}: {}", TOPIC_NAME, payload);
        } catch (Exception e) {
            log.error("Error publicando weather event: {}", e.getMessage(), e);
        } finally {
            try { if (producer   != null) producer.close();   } catch (JMSException ignored) { log.warn("Error cerrando producer: {}", ignored.getMessage()); }
            try { if (session    != null) session.close();    } catch (JMSException ignored) { log.warn("Error cerrando session: {}", ignored.getMessage()); }
            try { if (connection != null) connection.close(); } catch (JMSException ignored) { log.warn("Error cerrando connection: {}", ignored.getMessage()); }
        }
    }
}

