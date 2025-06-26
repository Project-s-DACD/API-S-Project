package org.example;

import org.example.adapters.EventBrokerConnection;
import org.example.adapters.EventFileStore;
import org.example.adapters.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Session;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    public Controller() {}

    public void start(String brokerUrl, String clientId, String baseDir, String... topics) {
        EventBrokerConnection broker = new EventBrokerConnection(brokerUrl, clientId);
        Session session = broker.getSession();

        EventListener listener = new EventListener(session, new EventFileStore(baseDir), clientId);

        for (String topic : topics) {
            log.info("Subscribing to topic: {}", topic);
            listener.subscribe(topic);
        }

        log.info("All topics subscribed. Controller is now listening...");

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Controller interrupted, shutting down...");
        }
    }
}
