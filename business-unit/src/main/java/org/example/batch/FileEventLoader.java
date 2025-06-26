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
    private static final Logger logger = LoggerFactory.getLogger(FileEventLoader.class);
    private final File baseDir;
    private final Gson gson = new Gson();

    public FileEventLoader(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public List<Flight> loadEvents() {
        List<Flight> allFlights = new ArrayList<>();

        if (!baseDir.exists()) {
            logger.error("Couldn´t find a path: {}", baseDir.getAbsolutePath());
            return allFlights;
        }

        File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".events"));
        if (files == null || files.length == 0) {
            logger.info("Couldn´t connect to .events in {}", baseDir.getAbsolutePath());
            return allFlights;
        }

        for (File file : files) {
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
                            logger.error("Wrong flight type null or no date: {}", data);
                        }

                        allFlights.add(flight);
                    } catch (Exception e) {
                        logger.error("Error in line: {}", line);
                    }
                }
            } catch (IOException e) {
                logger.error("Error while reading the file: {}", file.getName());
            }
        }

        logger.info("Event already saved: {}", allFlights.size());
        return allFlights;
    }
}
