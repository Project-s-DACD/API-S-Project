package org.example.infrastructure.ports;
import java.sql.SQLException;
import java.util.List;

public interface DataStore<T> {
    void insertFlightsIntoDatabase(List<T> items) throws SQLException;
}
