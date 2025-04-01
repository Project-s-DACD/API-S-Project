package org.example.apiServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AviationAPI extends AviationProvider {
    private final String apiKey;
    private final List<Flight> flights = new ArrayList<>();

    public AviationAPI(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public AviationAPI fetch() throws IOException {
        String responseBody = fetchData(getUrl());
        if (responseBody != null) {
            process(new JSONObject(responseBody));
        }
        return this;
    }

    @Override
    public List<Flight> flights() {
        return flights;
    }

    private void process(JSONObject jsonResponse) {
        if (!jsonResponse.has("data")) {
            return;
        }

        JSONArray flightData = jsonResponse.getJSONArray("data");
        for (int i = 0; i < flightData.length(); i++) {
            JSONObject flightJson = flightData.getJSONObject(i);
            Flight flight = new Flight(
                    flightJson.optString("flight_date", "N/A"),
                    flightJson.optString("flight_status", "N/A"),
                    flightJson.getJSONObject("departure").optString("airport", "N/A"),
                    flightJson.getJSONObject("arrival").optString("airport", "N/A"),
                    flightJson.getJSONObject("airline").optString("name", "N/A"),
                    flightJson.optString("flight_number", "N/A"),
                    i + 1
            );
            flights.add(flight);
        }
    }

    private String getUrl() {
        return "http://api.aviationstack.com/v1/flights?access_key=" + apiKey + "&arr_icao=GCLP&flight_status=landed";
    }
}
