package tqsua.airquality.models;

import java.util.Calendar;

public class CachedCity {
    private Calendar ttl;
    private final CityData data;

    public CachedCity(CityData data) {
        this.data = data;
        this.ttl = Calendar.getInstance();
        this.ttl.add(Calendar.MINUTE, 1);
    }

    public Calendar getTtl() {
        return ttl;
    }

    public void setTtl(Calendar ttl) {
        this.ttl = ttl;
    }

    public CityData getData() {
        return data;
    }
}
