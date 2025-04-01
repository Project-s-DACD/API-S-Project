package org.classes;


public class Indicator {
    private final String name;
    private final String url;

    public Indicator(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "📊 Indicator: " + name + "\nURL: " + url + "\n";
    }
}

