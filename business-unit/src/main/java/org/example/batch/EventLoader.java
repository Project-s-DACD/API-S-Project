package org.example.batch;


import org.example.domain.Flight;
import java.util.List;

public interface EventLoader {
    List<Flight> loadEvents();
}

