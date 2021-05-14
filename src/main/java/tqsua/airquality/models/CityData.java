package tqsua.airquality.models;

import java.util.Map;
import java.util.Objects;

public class CityData {
    private final Long idx;
    private final String name;
    private final String timestamp;

    // air quality values for the day
    private final Double no2;
    private final Double o3;
    private final Double pm10;
    private final Double pm25;
    private final Double so2;

    // temperature values for the dat

    public CityData(Long idx, String name, String timestamp, Map<String, Double> airQuality) {
        this.idx = idx;
        this.name = name;
        this.timestamp = timestamp;
        if (airQuality == null) {
            this.no2 = null;
            this.o3 = null;
            this.pm10 = null;
            this.pm25 = null;
            this.so2 = null;
        } else {
            this.no2 = airQuality.getOrDefault("no2", null);
            this.o3 = airQuality.getOrDefault("o3", null);
            this.pm10 = airQuality.getOrDefault("pm10", null);
            this.pm25 = airQuality.getOrDefault("pm25", null);
            this.so2 = airQuality.getOrDefault("so2", null);
        }
    }

    public Long getIdx() {
        return idx;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Double getNo2() {
        return no2;
    }

    public Double getO3() {
        return o3;
    }

    public Double getPm10() {
        return pm10;
    }

    public Double getPm25() {
        return pm25;
    }

    public Double getSo2() {
        return so2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityData cityData = (CityData) o;
        return Objects.equals(idx, cityData.idx) && Objects.equals(name, cityData.name) && Objects.equals(timestamp, cityData.timestamp) && Objects.equals(no2, cityData.no2) && Objects.equals(o3, cityData.o3) && Objects.equals(pm10, cityData.pm10) && Objects.equals(pm25, cityData.pm25) && Objects.equals(so2, cityData.so2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idx, name, timestamp, no2, o3, pm10, pm25, so2);
    }
}
