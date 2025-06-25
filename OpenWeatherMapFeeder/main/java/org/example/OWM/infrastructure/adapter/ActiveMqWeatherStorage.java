// src/main/java/org/example/OWM/infrastructure/adapter/ActiveMqWeatherStorage.java
package main.java.org.example.OWM.infrastructure.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import main.java.org.example.OWM.domain.LocationWeather;
import main.java.org.example.OWM.infrastructure.ports.WeatherStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.time.Instant;

/**
 * Envía objetos LocationWeather a una cola de ActiveMQ.
 */
public class ActiveMqWeatherStorage implements WeatherStorage {
    private static final Logger log = LoggerFactory.getLogger(ActiveMqWeatherStorage.class);

    private final Gson gson;
    private final ActiveMqManager manager;
    private final String queueName;

    public ActiveMqWeatherStorage(String brokerUrl, String queueName) {
        this.gson = new GsonBuilder()
                // si LocationWeather incluye Instant, registrar adaptador aquí
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.toString()))
                .create();
        this.manager = new ActiveMqManager(brokerUrl);
        this.queueName = queueName;
    }

    @Override
    public void save(LocationWeather weather) {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = manager.createConnection();
            session = manager.createSession(connection);
            producer = manager.createProducer(session, queueName);

            String payload = gson.toJson(weather);
            TextMessage message = session.createTextMessage(payload);
            message.setStringProperty("city", queueName);
            producer.send(message);
        } catch (Exception e) {
            log.error("Error publicando weather para {}: {}", queueName, e.getMessage(), e);
        } finally {
            try { if (producer   != null) producer.close();   } catch (JMSException ignored) { log.warn("Error cerrando producer: {}", ignored.getMessage()); }
            try { if (session    != null) session.close();    } catch (JMSException ignored) { log.warn("Error cerrando session: {}", ignored.getMessage()); }
            try { if (connection != null) connection.close(); } catch (JMSException ignored) { log.warn("Error cerrando connection: {}", ignored.getMessage()); }
        }
    }
}
