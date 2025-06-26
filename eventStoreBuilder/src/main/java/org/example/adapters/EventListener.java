// src/main/java/org/example/eventstorebuilder/adapters/EventListener.java
package org.example.adapters;

import javax.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener JMS que gestiona suscripciones durables a múltiples topics
 * y delega el procesamiento en un EventHandler.
 */
public class EventListener {
    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    private final Session session;
    private final EventHandler handler;
    private final String subscriptionBase;

    /**
     * @param session           Sesión JMS iniciada
     * @param handler           Manejador que procesará el JSON crudo
     * @param subscriptionBase  Prefijo para nombres de suscripción durable
     */
    public EventListener(Session session, EventHandler handler, String subscriptionBase) {
        this.session          = session;
        this.handler          = handler;
        this.subscriptionBase = subscriptionBase;
    }

    /**
     * Se suscribe durablemente al topic indicado.
     */
    public void subscribe(String topicName) {
        try {
            Topic topic = session.createTopic(topicName);
            // Nombre único de subscripción: prefijo + topic
            String subName = subscriptionBase + "-" + topicName;
            TopicSubscriber subscriber = session.createDurableSubscriber(topic, subName);
            subscriber.setMessageListener(msg -> {
                if (msg instanceof TextMessage textMsg) {
                    try {
                        handler.handle(topicName, textMsg.getText());
                    } catch (Exception e) {
                        log.error("Error handling message from topic {}: {}", topicName, e.getMessage(), e);
                    }
                }
            });
            log.info("Subscribed durable to topic {} with subscriptionId {}", topicName, subName);
        } catch (JMSException e) {
            throw new RuntimeException("Failed to subscribe durable to topic " + topicName, e);
        }
    }

    /**
     * Interfaz para procesar el JSON de cada evento.
     */
    public interface EventHandler {
        void handle(String topic, String rawJson) throws Exception;
    }
}