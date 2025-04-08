package software.classes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVIndicatorStore implements IndicatorStore {

    private final String filePath;

    public CSVIndicatorStore(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(List<Indicator> indicators) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Nombre,Link\n");
            for (Indicator ind : indicators) {
                writer.write(String.format("\"%s\",\"%s\"\n", ind.getName(), ind.getUrl()));
            }
            System.out.println("Indicadores guardados en CSV: " + filePath);
        } catch (IOException e) {
            System.err.println("Error guardando CSV: " + e.getMessage());
        }
    }
}