package org.example.sqlServices;
import java.sql.SQLException;
import java.util.List;

public interface DataStore<T> {
    void insert(List<T> items) throws SQLException;
}
