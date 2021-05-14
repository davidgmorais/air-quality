package tqsua.airquality;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tqsua.airquality.cache.CacheManager;
import tqsua.airquality.models.CachedCity;
import tqsua.airquality.models.CityData;

import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CacheManager_UnitTest {

    private CacheManager cacheManager = new CacheManager();

    @BeforeEach
    public void setUp() {
        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", 6.0);
        airQualityValues.put("o3", 32.5);
        airQualityValues.put("pm10", 8.0);
        airQualityValues.put("pm25", 14.0);
        airQualityValues.put("so2", 1.5);
        CityData lisbon = new CityData(
                1L,
                "Lisbon",
                "2021-05-05 15:00:00",
                airQualityValues);
        cacheManager.cacheCity(lisbon);
    }

    @AfterEach
    public void tearDown() {
        cacheManager = null;
    }

    @Test
    void whenGetCachedCityById_andEntryIsNotExpired_returnCity() {
        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", 6.0);
        airQualityValues.put("o3", 32.5);
        airQualityValues.put("pm10", 8.0);
        airQualityValues.put("pm25", 14.0);
        airQualityValues.put("so2", 1.5);
        CityData assertCity = new CityData(
                1L,
                "Lisbon",
                "2021-05-05 15:00:00",
                airQualityValues);
        assertEquals(cacheManager.getCityById(1L), assertCity);
    }

    @Test
    void whenGetCachedCityById_andEntryIsExpired_returnNull() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -3);
        cacheManager.setTtl(1L, c);
        assertNull(cacheManager.getCityById(1L));
    }

    @Test
    void whenGetCachedCityById_andEntryDoesNotExist_returnNull() {
        assertNull(cacheManager.getCityById(10L));
    }

    @Test
    void assertIncrementMiss() {
        cacheManager.getCityById(100L);
        assertEquals(1, cacheManager.getMiss());
    }

    @Test
    void assertIncrementHit() {
        cacheManager.getCityById(1L);
        assertEquals(1, cacheManager.getHit());
    }

    @Test
    void assertIncrementRequestTotal_whenHit() {
        cacheManager.getCityById(1L);
        assertEquals(1, cacheManager.getRequestTotal());
    }

    @Test
    void assertIncrementRequestTotal_whenMiss() {
        cacheManager.getCityById(100L);
        assertEquals(1, cacheManager.getRequestTotal());
    }

    @Test
    void whenSetTtl_andCityIdDoesNotExist_raiseException() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -3);

        assertThrows(NullPointerException.class,
                () -> cacheManager.setTtl(5L, c)
        );
    }

    @Test
    void whenGetCache_andNoExpiredEntries_returnAll() {
        List<CachedCity> inCache = cacheManager.getAllEntries();
        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", 6.0);
        airQualityValues.put("o3", 32.5);
        airQualityValues.put("pm10", 8.0);
        airQualityValues.put("pm25", 14.0);
        airQualityValues.put("so2", 1.5);
        CityData assertCity = new CityData(
                1L,
                "Lisbon",
                "2021-05-05 15:00:00",
                airQualityValues);

        assertEquals(1, inCache.size());
        assertEquals(assertCity, inCache.get(0).getData());
        assertEquals(assertCity.hashCode(), inCache.get(0).getData().hashCode());
    }

    @Test
    void whenGetCache_andExpiredEntries_returnAllNotExpiredEntries() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -3);
        cacheManager.setTtl(1L, c);
        List<CachedCity> inCache = cacheManager.getAllEntries();
        assertTrue(inCache.isEmpty());
    }

    @Test
    void whenGetCachedCityByName_andEntryIsNotExpired_returnCity() {
        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", 6.0);
        airQualityValues.put("o3", 32.5);
        airQualityValues.put("pm10", 8.0);
        airQualityValues.put("pm25", 14.0);
        airQualityValues.put("so2", 1.5);
        CityData assertCity = new CityData(
                1L,
                "Lisbon",
                "2021-05-05 15:00:00",
                airQualityValues);
        assertEquals(cacheManager.getCityByName("Lisbon"), assertCity);
    }

    @Test
    void whenGetCachedCityByName_andEntryIsExpired_returnNull() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -3);
        cacheManager.setTtl(1L, c);
        assertNull(cacheManager.getCityByName("Lisbon"));
    }

    @Test
    void whenGetCachedCityByName_andEntryDoesNotExist_returnNull() {
        assertNull(cacheManager.getCityByName("Viseu"));
    }


}
