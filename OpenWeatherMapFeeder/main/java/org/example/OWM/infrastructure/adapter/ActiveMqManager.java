package org.example.OWM.infrastructure.adapter;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;


public class ActiveMqManager {

    private final String brokerUrl;

    public ActiveMqManager(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public Connection createConnection() throws javax.jms.JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        javax.jms.Connection Connection = factory.createConnection();
        Connection.start();
        return Connection;
    }

    public Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
}