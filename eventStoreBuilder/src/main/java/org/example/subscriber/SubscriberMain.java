package org.example.subscriber;

public class SubscriberMain {
    public static void main(String[] args) {

        try {
            new FlightSubscriber().start();
        } catch (Exception e) {
            System.err.println("Error al iniciar el subscriber: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
