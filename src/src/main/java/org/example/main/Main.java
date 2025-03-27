package org.example.main;

import org.example.data.AviationCreateTable;
import org.example.flightapi.AviationAPI;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            AviationAPI api = new AviationAPI(System.getenv("apiKey")).fetch();
            AviationCreateTable db = new AviationCreateTable(new File("flights.db"));
            db.insert(api.flights());
            System.out.println("Vuelos procesados y almacenados exitosamente.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error al obtener datos de la API: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
