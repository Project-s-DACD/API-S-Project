package org.example.batch;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.domain.Flight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileEventLoader implements EventLoader {
    private final File baseDir;
    private final Gson gson = new Gson();

    public FileEventLoader(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public List<Flight> loadEvents() {
        List<Flight> allFlights = new ArrayList<>();

        if (!baseDir.exists()) {
            System.err.println("Couldn´t find a path: " + baseDir.getAbsolutePath());
            return allFlights;
        }

        File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".events"));
        if (files == null || files.length == 0) {
            System.out.println("Couldn´t connect to .events in " + baseDir.getAbsolutePath());
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
                            System.err.println("Wrong flight type null or no date: " + data);
                        }

                        allFlights.add(flight);
                    } catch (Exception e) {
                        System.err.println("Error in line: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error whule reading the file: " + file.getName());
            }
        }

        System.out.println("Event already saved: " + allFlights.size());
        return allFlights;
    }
}

