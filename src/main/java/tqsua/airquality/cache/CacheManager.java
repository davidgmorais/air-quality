package tqsua.airquality.cache;

import org.springframework.context.annotation.Configuration;
import tqsua.airquality.models.CachedCity;
import tqsua.airquality.models.CityData;
import java.util.*;

@Configuration
public class CacheManager {
    private int hit;
    private int miss;
    private int requestTotal;
    private final Map<Long, CachedCity> cityDataCache;

    public CacheManager() {
        this.hit = 0;
        this.miss = 0;
        this.requestTotal = 0;
        this.cityDataCache = new HashMap<>();
    }

    public int getHit() {
        return hit;
    }

    public int getMiss() {
        return miss;
    }

    public int getRequestTotal() {return this.requestTotal;}

    private void incrementHit() {this.hit++;}

    private void incrementMiss() {
        this.miss++;
    }

    private void incrementRequestTotal() {this.requestTotal++;}

    public void setTtl(Long id, Calendar newTtl) {
        this.cityDataCache.get(id).setTtl(newTtl);
    }

    public void cacheCity(CityData data) {
        this.cityDataCache.put(data.getIdx(), new CachedCity(data));
    }

    public CityData getCityById(long cityId) {
        this.incrementRequestTotal();
        if (this.cityDataCache.containsKey(cityId) && !isExpired(cityId)) {
            this.incrementHit();
            return this.cityDataCache.get(cityId).getData();
        }
        this.incrementMiss();
        return null;
    }

    public CityData getCityByName(String cityName) {
        this.incrementRequestTotal();
        for (Map.Entry<Long, CachedCity> entry: this.cityDataCache.entrySet()) {
            if (entry.getValue().getData().getName().equals(cityName) && !isExpired(entry.getKey())) {
                this.incrementHit();
                return entry.getValue().getData();
            }
        }
        this.incrementMiss();
        return null;
    }



    private boolean isExpired(Long cityId) {
        Calendar currentInstance = Calendar.getInstance();
        boolean expired = currentInstance.after(this.cityDataCache.get(cityId).getTtl());
        if (expired) {
            this.cityDataCache.remove(cityId);
        }
        return expired;
    }

    public List<CachedCity> getAllEntries() {
        this.incrementRequestTotal();
        for (Long cityIdx: this.cityDataCache.keySet()) {
           this.isExpired(cityIdx);
        }
        return new ArrayList<>(this.cityDataCache.values());
    }
}
