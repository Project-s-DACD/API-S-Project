package org.example.apiServices;

import java.io.IOException;
import java.util.List;

public interface DataProvider {
    DataProvider fetch() throws IOException;
    List<Flight> flights();
}
