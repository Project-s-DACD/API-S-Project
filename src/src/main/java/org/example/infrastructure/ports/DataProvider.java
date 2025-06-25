package org.example.infrastructure.ports;

import org.example.domain.Flight;

import java.io.IOException;
import java.util.List;

public interface DataProvider {
    DataProvider fetchDataFromApi() throws IOException;
    List<Flight> flights();
}
