package org.example.infrastructure;

import org.example.domain.Flight;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AviationAPI extends AviationProvider {
    private final String apiUrl;
    private final List<Flight> flights = new ArrayList<>();

    public AviationAPI(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    public AviationAPI fetchDataFromApi() throws IOException {
        String responseBody = fetchData(getUrlFromApi());
        if (responseBody != null) {
            processInformationFromApiChangingToFlight(new JSONObject(responseBody));
        }
        return this;
    }

    @Override
    public List<Flight> flights() {
        return flights;
    }

    private void processInformationFromApiChangingToFlight(JSONObject jsonResponse) {
        if (!jsonResponse.has("data")) {
            return;
        }

        JSONArray flightData = jsonResponse.getJSONArray("data");

        for (int i = 0; i < flightData.length(); i++) {
            JSONObject flightJson = flightData.getJSONObject(i);

            Integer delay = null;
            JSONObject departure = flightJson.optJSONObject("departure");
            if (departure != null && !departure.isNull("delay")) {
                delay = departure.optInt("delay");
            }

            Flight flight = new Flight(
                    flightJson.optString("flight_date", "N/A"),
                    flightJson.optString("flight_status", "N/A"),
                    departure != null ? departure.optString("airport", "N/A") : "N/A",
                    flightJson.optJSONObject("arrival") != null
                            ? flightJson.getJSONObject("arrival").optString("airport", "N/A")
                            : "N/A",
                    flightJson.optJSONObject("airline") != null
                            ? flightJson.getJSONObject("airline").optString("name", "N/A")
                            : "N/A",
                    flightJson.optJSONObject("flight") != null
                            ? flightJson.getJSONObject("flight").optString("number", "N/A")
                            : "N/A",
                    i + 1,
                    delay
            );

            flights.add(flight);
        }
    }

    private String getUrlFromApi() {
        return apiUrl;
    }
}
