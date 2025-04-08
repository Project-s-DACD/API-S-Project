package software.main;

import org.classes.*;
import software.classes.GobCanariasAPIProvider;
import software.classes.Indicator;
import software.classes.IndicatorProvider;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        IndicatorProvider provider = new GobCanariasAPIProvider();
        List<Indicator> indicators = provider.fetchIndicators();

        System.out.println("📊 Indicadores obtenidos:");
        for (Indicator indicator : indicators) {
            System.out.println("- " + indicator.getName() + " | 🔗 " + indicator.getUrl());
        }
    }
}