package org.example.weather;

import org.example.weather.scheduler.WeatherScheduler;

public class WeatherApp {
    public static void main(String[] args) throws Exception {
        String city = args.length > 0 ? args[0] : "Madrid,ES";

        WeatherScheduler scheduler = new WeatherScheduler(city);
        scheduler.start();

        System.out.printf("Iniciando monitor de clima para %s %n", city);

        Thread.currentThread().join();
    }
}
