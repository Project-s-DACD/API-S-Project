package org.example.infrastructure.apiServices;

import org.example.domain.Flight;

import java.io.IOException;
import java.util.List;

public interface DataProvider {
    DataProvider fetchDataFromApi() throws IOException;
    List<Flight> flights();
}
