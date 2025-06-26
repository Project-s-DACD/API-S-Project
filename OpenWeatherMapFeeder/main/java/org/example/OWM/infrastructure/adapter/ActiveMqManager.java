package org.example.OWM.infrastructure.adapter;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMqManager {

    private final String brokerUrl;

    public ActiveMqManager(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public Connection createConnection() throws JMSException, jakarta.jms.JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = (Connection) factory.createConnection();
        connection.start();
        return connection;
    }

    public Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public MessageProducer createProducer(Session session, String queueName) throws JMSException {
        Queue queue = session.createQueue(queueName);
        return session.createProducer(queue);
    }
}