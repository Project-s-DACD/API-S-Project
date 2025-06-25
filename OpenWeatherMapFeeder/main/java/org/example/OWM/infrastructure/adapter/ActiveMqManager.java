package main.java.org.example.OWM.infrastructure.adapter;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Simplified Connection Manager for ActiveMQ, adapted to use queues.
 */
public class ActiveMqManager {

    private final String brokerUrl;

    public ActiveMqManager(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    /**
     * Crea y arranca una conexión JMS.
     */
    public Connection createConnection() throws JMSException, jakarta.jms.JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = (Connection) factory.createConnection();
        connection.start();
        return connection;
    }

    /**
     * Crea una sesión no transaccional con ACK automático.
     */
    public Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * Crea un MessageProducer para la cola indicada.
     */
    public MessageProducer createProducer(Session session, String queueName) throws JMSException {
        Queue queue = session.createQueue(queueName);
        return session.createProducer(queue);
    }
}