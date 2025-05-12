package org.example.serving;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class BusinessCli {

    private final Scanner scanner = new Scanner(System.in);

    public void startMenuCli() {
        String opcion;
        do {
            System.out.println("/n Menú de Análisis de Vuelos");
            System.out.println("1. Show Airpor´s Wordcloud");
            System.out.println("2. Show airlines");
            System.out.println("3. exit");
            System.out.print("Choose an option: ");
            opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> showGraph("C:/Users/agust/OneDrive/Escritorio/Uni/2º año/DAPC/Api´s project/business-unit/graficos/grafico_test2.png");
                case "2" -> showGraph("C:/Users/agust/OneDrive/Escritorio/Uni/2º año/DAPC/Api´s project/business-unit/graficos/grafico_test.png");
                case "3" -> System.out.println("Saliendo del CLI...");
                default -> System.out.println("Opción no válida. Intentá de nuevo.");
            }
        } while (!opcion.equals("3"));
    }

    public void showGraph(String ruta) {
        try {
            File grafico = new File(ruta);
            if (!grafico.exists()) {
                System.out.println("No graphic found: " + ruta);
                return;
            }
            Desktop.getDesktop().open(grafico);
            System.out.println("Graph opened: " + ruta);
        } catch (IOException e) {
            System.err.println("Error al abrir el gráfico: " + e.getMessage());
        }
    }
}
