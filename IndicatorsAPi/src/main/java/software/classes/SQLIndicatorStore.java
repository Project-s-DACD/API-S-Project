package software.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class SQLIndicatorStore implements IndicatorStore {

    private final String dbUrl;

    public SQLIndicatorStore(String dbUrl) {
        this.dbUrl = dbUrl;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS indicators (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                link TEXT NOT NULL
            );
        """;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'indicators' lista en la base de datos.");
        } catch (Exception e) {
            System.err.println("Error creando tabla: " + e.getMessage());
        }
    }

    @Override
    public void save(List<Indicator> indicators) {
        String insertSQL = "INSERT INTO indicators(name, link) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            for (Indicator ind : indicators) {
                pstmt.setString(1, ind.getName());
                pstmt.setString(2, ind.getUrl());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Indicadores guardados en la base de datos.");
        } catch (Exception e) {
            System.err.println("Error guardando en DB: " + e.getMessage());
        }
    }
}
