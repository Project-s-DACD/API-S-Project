package org.classes;

import org.classes.Indicator;

import java.util.List;

public interface IndicatorProvider {
    List<Indicator> fetchIndicators();
}
