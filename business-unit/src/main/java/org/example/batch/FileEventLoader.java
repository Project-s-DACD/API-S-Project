package org.example.batch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.domain.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileEventLoader {
    private static final Logger log = LoggerFactory.getLogger(FileEventLoader.class);
    private final File baseDir;

    public FileEventLoader(File baseDir) {
        this.baseDir = baseDir;
    }

    public List<Flight> loadEvents() {
        List<Flight> allFlights = new ArrayList<>();
        if (!baseDir.exists()) {
            log.error("Path not found: {}", baseDir.getAbsolutePath());
            return allFlights;
        }

        List<File> eventFiles = findAllEventFiles(baseDir);
        if (eventFiles.isEmpty()) {
            log.warn("No .events files found in: {}", baseDir.getAbsolutePath());
            return allFlights;
        }

        for (File file : eventFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        JsonObject event = JsonParser.parseString(line).getAsJsonObject();

                        String ts = event.get("ts").getAsString();   // ts del evento
                        String ss = event.get("ss").getAsString();   // ss del evento
                        JsonObject data = event.getAsJsonObject("data");

                        // Crear Flight con constructor completo
                        Flight flight = new Flight(
                                data.get("flight_date").getAsString(),
                                data.get("flight_status").getAsString(),
                                data.get("departure_airport").getAsString(),
                                data.get("arrival_airport").getAsString(),
                                data.get("airline").getAsString(),
                                data.has("flightNumber") ? data.get("flightNumber").getAsString() : "",
                                data.has("id") ? data.get("id").getAsInt() : 0,
                                data.has("departure_delay") && !data.get("departure_delay").isJsonNull()
                                        ? data.get("departure_delay").getAsInt() : 0,
                                ts,
                                ss
                        );

                        allFlights.add(flight);

                    } catch (Exception e) {
                        log.error("Error parsing line in {}: {}", file.getName(), e.getMessage());
                    }
                }
            } catch (IOException e) {
                log.error("Error reading file {}: {}", file.getName(), e.getMessage());
            }
        }

        log.info("Total flights loaded from .events: {}", allFlights.size());
        return allFlights;
    }

    private List<File> findAllEventFiles(File dir) {
        List<File> result = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files == null) return result;

        for (File file : files) {
            if (file.isDirectory()) {
                result.addAll(findAllEventFiles(file));
            } else if (file.isFile() && file.getName().endsWith(".events")) {
                result.add(file);
            }
        }
        return result;
    }
}
