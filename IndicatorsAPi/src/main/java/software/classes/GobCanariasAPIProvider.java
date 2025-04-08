package software.classes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GobCanariasAPIProvider implements IndicatorProvider {

    private static final String API_URL = "https://datos.canarias.es/api/estadisticas/indicators/v1.0/indicatorsSystems/C00075A";

    @Override
    public List<Indicator> fetchIndicators() {
        List<Indicator> indicators = new ArrayList<>();
        try {
            String json = getJsonFromApi(API_URL);
            JSONObject root = new JSONObject(json);
            JSONArray elements = root.getJSONArray("elements");

            for (int i = 0; i < elements.length(); i++) {
                JSONObject dimension = elements.getJSONObject(i);
                extractIndicatorsFromDimension(dimension, indicators);
            }

        } catch (Exception e) {
            System.err.println("Error fetching indicators: " + e.getMessage());
        }
        return indicators;
    }

    private void extractIndicatorsFromDimension(JSONObject dimension, List<Indicator> indicators) {
        if (dimension.has("elements")) {
            JSONArray children = dimension.getJSONArray("elements");
            for (int i = 0; i < children.length(); i++) {
                JSONObject child = children.getJSONObject(i);
                if ("indicators#indicatorInstance".equals(child.getString("kind"))) {
                    String title = child.getJSONObject("title").getString("es");
                    String link = child.getString("selfLink");
                    indicators.add(new Indicator(title, link)); // Asegúrate que tu clase `Indicator` tenga este constructor
                } else if ("indicators#dimension".equals(child.getString("kind"))) {
                    extractIndicatorsFromDimension(child, indicators); // recursión
                }
            }
        }
    }

    private String getJsonFromApi(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        reader.close();
        conn.disconnect();

        return responseBuilder.toString();
    }
}