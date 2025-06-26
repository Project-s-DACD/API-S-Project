package org.example;

import org.example.adapters.EventBrokerConnection;
import org.example.adapters.EventListener;
import org.example.adapters.EventFileStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Session;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    /**
     * Arranca las suscripciones durables a varios topics.
     * @param brokerUrl   URL del broker ActiveMQ (tcp://host:61616)
     * @param clientId    Identificador único para las suscripciones durables
     * @param baseDir     Directorio raíz donde escribir los ficheros (ej. "eventstore")
     * @param topics      Lista de topics a los que suscribirse
     */
    public void start(String brokerUrl, String clientId, String baseDir, String... topics) {
        // Conexión y sesión
        EventBrokerConnection broker = new EventBrokerConnection(brokerUrl, clientId);
        Session session = broker.getSession();

        // Creamos el listener con FileEventStore como handler
        EventListener listener = new EventListener(
                session,
                new EventFileStore(baseDir),
                clientId
        );

        // Suscribirse a cada topic
        for (String topic : topics) {
            log.info("Subscribing to topic: {}", topic);
            listener.subscribe(topic);
        }

        // No cerramos la conexión; dejamos viva la app
        log.info("Subscription setup complete. Listening for messages...");
    }
}
