package org.example.serving;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class BusinessCli {

    private static final Logger logger = LoggerFactory.getLogger(BusinessCli.class);
    private final Scanner scanner = new Scanner(System.in);

    public void startMenuCli() {
        String opcion;
        do {
            logger.info("\n -> Menú de Análisis de Vuelos");
            logger.info("1. Show Airpor´s Wordcloud");
            logger.info("2. Show airlines");
            logger.info("3. Show Actual Weather");
            logger.info("4. Open Visual Interface");
            logger.info("5. Show Weather vs Delay");
            logger.info("0. exit");
            logger.info("Choose an option: ");
            opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> showGraph("business-unit/graficos/grafico_test2.png");
                case "2" -> showGraph("business-unit/graficos/grafico_test.png");
                case "3" -> showGraph("business-unit/graficos/grafico_weather.png");
                case "4" -> SwingUtilities.invokeLater(VisualInterfaceGui::new);
                case "5" -> showGraph("business-unit/graficos/grafico_weathervsdelay.png");
                case "0" -> logger.info("Exit from CLI...");
                default -> logger.info("Invalid option. Try again");
            }
        } while (!opcion.equals("3"));
    }

    public void showGraph(String ruta) {
        try {
            File grafico = new File(ruta);
            if (!grafico.exists()) {
                logger.warn("No graphic found: {}", ruta);
                return;
            }
            Desktop.getDesktop().open(grafico);
            logger.info("Graph opened: {}", ruta);
        } catch (IOException e) {
            logger.error("Error opening the graph: {}", e.getMessage());
        }
    }
}
