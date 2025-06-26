package org.example.serving;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class DatamartFlightStore {
    private static final Logger logger = LoggerFactory.getLogger(DatamartFlightStore.class);

    private final File dbFile;

    public DatamartFlightStore(File file) {
        this.dbFile = file;
    }

    public void executeScriptWithProcessBuilder() {
        try {
            String rutaScript = "C:/Users/agust/OneDrive/Escritorio/Uni/2º año/DAPC/Api´s project/business-unit/graficos/generarGraficos.R";

            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\PROGRA~1\\R\\R-44~1.1\\bin\\x64\\Rscript.exe",
                    rutaScript
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines()
                    .forEach(logger::info);

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Script R is executing correctly.");
            } else {
                logger.error("Script R already finished: {}", exitCode);
            }

        } catch (Exception e) {
            logger.error("Couldn´t execute Rscript: {}", e.getMessage());
        }
    }
}
