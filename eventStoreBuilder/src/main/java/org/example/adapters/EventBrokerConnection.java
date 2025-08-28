package org.example.adapters;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class EventBrokerConnection {
    private final Connection connection;
    private final Session    session;

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


    public Session getSession() {
        return session;
    }


    public void close() {
        try {
            if (session    != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException ignore) {

        }
    }
}