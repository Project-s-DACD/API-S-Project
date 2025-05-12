package org.example.subscriber;

import javax.jms.JMSException;

public class SubscriberMain {
    public static void main(String[] args) {
        try {
            FlightSubscriber subscriber = new FlightSubscriber();
            subscriber.startConnectionToBroker();
        } catch (JMSException e) {
            System.err.println("Error while connecting to ActiveMQ.");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error while running the subscription.");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
