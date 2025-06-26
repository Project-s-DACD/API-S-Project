package org.example.batch;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.domain.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileEventLoader implements EventLoader {
    private static final Logger log = LoggerFactory.getLogger(FileEventLoader.class);

    private final File baseDir;
    private final Gson gson = new Gson();

    public FileEventLoader(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
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
                        JsonObject data = event.getAsJsonObject("data");
                        Flight flight = gson.fromJson(data, Flight.class);
                        if (flight != null && flight.getFlight_date() != null) {
                            allFlights.add(flight);
                        } else {
                            log.warn("Skipping invalid flight: {}", data);
                        }
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
