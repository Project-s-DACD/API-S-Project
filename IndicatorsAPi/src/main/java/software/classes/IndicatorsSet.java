package software.classes;

import java.util.List;
import java.util.Collections;

public class IndicatorsSet {
    private final List<Indicator> indicators;

    public IndicatorsSet(List<Indicator> indicators) {
        this.indicators = Collections.unmodifiableList(indicators);
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }
}