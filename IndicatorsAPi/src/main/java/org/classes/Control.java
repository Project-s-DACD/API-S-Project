package org.classes;

import org.classes.IndicatorsSet;
import org.classes.IndicatorProvider;

import java.util.List;

public class Control {
    private final IndicatorProvider provider;

    public Control(IndicatorProvider provider) {
        this.provider = provider;
    }

    public IndicatorsSet getIndicators() {
        List<Indicator> indicators = provider.fetchIndicators();
        return new IndicatorsSet(indicators);
    }
}