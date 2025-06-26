// src/main/java/es/ulpgc/dacd/eventstorebuilder/adapters/EventBrokerConnection.java
package org.example.adapters;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Simplified Connection Manager for ActiveMQ with durable subscriber support.
 */
public class EventBrokerConnection {
    private final Connection connection;
    private final Session    session;

    /**
     * @param brokerUrl URL de conexión al broker (tcp://host:61616)
     * @param clientId  Identificador único para suscripciones durables
     */
    public EventBrokerConnection(String brokerUrl, String clientId) {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            this.connection = factory.createConnection();
            this.connection.setClientID(clientId);
            this.connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new RuntimeException("Failed to connect to broker: " + brokerUrl, e);
        }
    }

    /**
     * Devuelve la sesión JMS ya iniciada.
     */
    public Session getSession() {
        return session;
    }

    /**
     * Cierra la conexión y la sesión.
     */
    public void close() {
        try {
            if (session    != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException ignore) {
            // Ignorar errores al cerrar
        }
    }
}