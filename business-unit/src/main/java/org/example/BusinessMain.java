package org.example;

import org.example.batch.EventLoader;
import org.example.batch.FileEventLoader;
import org.example.domain.Flight;
import org.example.serving.BusinessCli;
import org.example.serving.DatamartFlightStore;
import org.example.speed.BusinessSubscriber;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class BusinessMain {

    public static void main(String[] args) {
        try {

            DatamartFlightStore datamart = new DatamartFlightStore(new File("business-unit/datamart.db"));

            // Carga eventos históricos
            System.out.println("📂 Ruta del archivo de eventos: " + args[0]);
            EventLoader loader = new FileEventLoader(new File(args[0]));
            List<Flight> vuelosHistoricos = loader.loadEvents();
            List<Flight> vuelosValidos = vuelosHistoricos.stream()
                    .filter(f -> f != null && f.getFlight_date() != null)
                    .toList();

            System.out.println("✅ Vuelos válidos para insertar: " + vuelosValidos.size());
            datamart.insertFlightsIntoDatabase(vuelosValidos);

            // Subscripción en tiempo real (para nuevos eventos del broker)
            BusinessSubscriber subscriber = new BusinessSubscriber(datamart);
            subscriber.startRealTimeSubscriber();

            // CLI para visualización de gráficos
            new BusinessCli().startMenuCli();

            // Mantiene la app viva
            System.out.println("🟢 Escuchando eventos en tiempo real...");
            Thread.sleep(Long.MAX_VALUE);

        } catch (SQLException e) {
            System.err.println("❌ Error con la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
