
package org.example.adapters;

import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class EventFileStore implements EventListener.EventHandler {
    private final String baseDir;
    private final Gson   gson;

    public EventFileStore(String baseDir) {
        this.baseDir = baseDir;
        this.gson    = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantDeserializer())
                .create();
    }

    @Override
    public void handle(String topic, String rawJson) throws IOException {
        JsonObject obj = gson.fromJson(rawJson, JsonObject.class);
        String ts = obj.get("ts").getAsString();
        String ss = obj.get("ss").getAsString();

        String date = LocalDate
                .parse(ts.substring(0, 10))
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Path dir = Paths.get(baseDir, topic, ss);
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
