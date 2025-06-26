package org.example.adapters;

import javax.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListener {
    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    private final Session session;
    private final EventHandler handler;
    private final String subscriptionBase;

    public EventListener(Session session, EventHandler handler, String subscriptionBase) {
        this.session = session;
        this.handler = handler;
        this.subscriptionBase = subscriptionBase;
    }

    public void subscribe(String topicName) {
        try {
            Topic topic = session.createTopic(topicName);
            String subName = (subscriptionBase + "-" + topicName).replaceAll("[^a-zA-Z0-9_-]", "_");

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

    public interface EventHandler {
        void handle(String topic, String rawJson) throws Exception;
    }
}
