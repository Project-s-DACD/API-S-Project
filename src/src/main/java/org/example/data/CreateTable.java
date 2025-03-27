package org.example.data;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public abstract class CreateTable<T> {
    protected final File file;
    public CreateTable(File file) throws SQLException {
        this.file = file;
        this.createDatabase();
    }
    protected abstract void createDatabase() throws SQLException;

    public void insert(List<T> items) throws SQLException {
        if (items.isEmpty()) {
            System.out.println("No hay vuelos disponibles para insertar.");
            return;
        }
        saveFlightsToDatabase(items);
    }

    protected abstract void saveFlightsToDatabase(List<T> items) throws SQLException;

    protected String dbUrl() {
        return "jdbc:sqlite:" + file.getAbsolutePath();
    }
}
