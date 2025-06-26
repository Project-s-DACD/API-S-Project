// src/main/java/es/ulpgc/dacd/eventstorebuilder/adapters/FileEventStore.java
package org.example.adapters;

import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Maneja la deserialización de eventos JSON y su almacenamiento en ficheros
 * bajo eventstore/{topic}/{ss}/{YYYYMMDD}.events
 */
public class EventFileStore implements EventListener.EventHandler {
    private final Gson gson;

    public EventFileStore(String baseDir) {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantDeserializer())
                .create();
    }

    @Override
    public void handle(String topic, String rawJson) throws IOException {
        JsonObject obj = gson.fromJson(rawJson, JsonObject.class);
        String ts = obj.get("ts").getAsString();
        String ss = obj.get("ss").getAsString();

        // Extraemos la fecha YYYYMMDD del campo ts (ISO-8601)
        String date = LocalDate.parse(ts.substring(0, 10))
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Path dir = Paths.get("eventstore", topic, ss);
        Files.createDirectories(dir);

        Path file = dir.resolve(date + ".events");
        Files.writeString(
                file,
                rawJson + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    private static class InstantDeserializer implements JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context)
                throws JsonParseException {
            return Instant.parse(json.getAsString());
        }
    }
}
