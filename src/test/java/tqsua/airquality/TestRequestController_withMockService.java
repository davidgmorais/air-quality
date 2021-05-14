package tqsua.airquality;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqsua.airquality.cache.CacheManager;
import tqsua.airquality.models.CachedCity;
import tqsua.airquality.models.CityData;
import tqsua.airquality.services.RequestServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;


@WebMvcTest
class TestRequestController_withMockService {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RequestServiceImpl service;
    @MockBean
    private CacheManager cache;

    @BeforeEach
    void setUp() {}

    @Test
    void givenExistingCity_andCityNotInCache_whenGetCityById_returnCity() throws Exception {
        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", 6.0);
        airQualityValues.put("o3", 32.5);
        airQualityValues.put("pm10", 8.0);
        airQualityValues.put("pm25", 14.0);
        airQualityValues.put("so2", 1.5);
        CityData city = new CityData(
                8379L,
                "Entrecampos, Lisboa, Portugal",
                "2021-05-05 15:00:00",
                airQualityValues);
        given(service.getCityDataById(8379L)).willReturn(city);
        given(cache.getCityById(8379L)).willReturn(null);
        mvc.perform(get("/api/city/8379").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx", is(city.getIdx().intValue())))
                .andExpect(jsonPath("$.name", is(city.getName())))
                .andExpect(jsonPath("$.timestamp", is(city.getTimestamp())))
                .andExpect(jsonPath("$.no2", is(city.getNo2())))
                .andExpect(jsonPath("$.o3", is(city.getO3())))
                .andExpect(jsonPath("$.pm10", is(city.getPm10())))
                .andExpect(jsonPath("$.pm25", is(city.getPm25())))
                .andExpect(jsonPath("$.so2", is(city.getSo2())));
        verify(service, VerificationModeFactory.times(1)).getCityDataById(Mockito.anyLong());
    }

    @Test
    void givenNonExistingCity_andCityNotInCache_whenGetCityById_returnNotFound() throws Exception {
        given(service.getCityDataById(1L)).willReturn(null);
        given(cache.getCityById(1)).willReturn(null);
        mvc.perform(get("/api/city/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getCityDataById(Mockito.anyLong());
    }

    @Test
    void givenExistingCity_andCityInCache_whenGetCityById_returnCity() throws Exception {
        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", 6.0);
        airQualityValues.put("o3", 32.5);
        airQualityValues.put("pm10", 8.0);
        airQualityValues.put("pm25", 14.0);
        airQualityValues.put("so2", 1.5);
        CityData city = new CityData(
                8379L,
                "Entrecampos, Lisboa, Portugal",
                "2021-05-05 15:00:00",
                airQualityValues);
        given(service.getCityDataById(8379L)).willReturn(city);
        given(cache.getCityById(8379L)).willReturn(city);
        mvc.perform(get("/api/city/8379").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx", is(city.getIdx().intValue())))
                .andExpect(jsonPath("$.name", is(city.getName())))
                .andExpect(jsonPath("$.timestamp", is(city.getTimestamp())))
                .andExpect(jsonPath("$.no2", is(city.getNo2())))
                .andExpect(jsonPath("$.o3", is(city.getO3())))
                .andExpect(jsonPath("$.pm10", is(city.getPm10())))
                .andExpect(jsonPath("$.pm25", is(city.getPm25())))
                .andExpect(jsonPath("$.so2", is(city.getSo2())));
        verify(service, VerificationModeFactory.times(0)).getCityDataById(Mockito.anyLong());
    }

    @Test
    void givenNoCitiesInCache_whenGetCacheStats_returnCacheStats() throws Exception {
        given(cache.getAllEntries()).willReturn(new ArrayList<>());
        given(cache.getHit()).willReturn(1);
        given(cache.getMiss()).willReturn(2);
        given(cache.getRequestTotal()).willReturn(3);

        mvc.perform(get("/api/cache").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.RequestTotal", is(3)))
                .andExpect(jsonPath("$.Hits", is(1)))
                .andExpect(jsonPath("$.Misses", is(2)))
                .andExpect(jsonPath("$.InCache", hasSize(0)));
    }

    @Test
    void givenCitiesInCache_whenGetCacheStats_returnCacheStats() throws Exception {
        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", 6.0);
        airQualityValues.put("o3", 32.5);
        airQualityValues.put("pm10", 8.0);
        airQualityValues.put("pm25", 14.0);
        airQualityValues.put("so2", 1.5);
        CityData city = new CityData(
                8379L,
                "Entrecampos, Lisboa, Portugal",
                "2021-05-05 15:00:00",
                airQualityValues);
        ArrayList<CachedCity> entries = new ArrayList<>();
        entries.add(new CachedCity(city));
        given(cache.getAllEntries()).willReturn(entries);

        given(cache.getHit()).willReturn(1);
        given(cache.getMiss()).willReturn(2);
        given(cache.getRequestTotal()).willReturn(3);

        mvc.perform(get("/api/cache").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.RequestTotal", is(3)))
                .andExpect(jsonPath("$.Hits", is(1)))
                .andExpect(jsonPath("$.Misses", is(2)))
                .andExpect(jsonPath("$.InCache", hasSize(1)))
                .andExpect(jsonPath("$.InCache[0].data.idx", is(city.getIdx().intValue())));
    }

    @Test
    void givenResults_whenSearchQuery_returnSearchResults() throws Exception {
        HashMap<Long, String> searchResults = new HashMap<>();
        searchResults.put(10513L, "Olivais, Lisboa, Portugal");
        searchResults.put(8379L, "Entrecampos, Lisboa, Portugal");
        searchResults.put(8381L, "lisboa; Laranjeiro, Almada, Portugal");
        given(service.searchCity("lisboa")).willReturn(searchResults);

        mvc.perform(get("/api/search/lisboa").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['10513']", is("Olivais, Lisboa, Portugal")))
                .andExpect(jsonPath("$['8379']", is("Entrecampos, Lisboa, Portugal")))
                .andExpect(jsonPath("$['8381']", is("lisboa; Laranjeiro, Almada, Portugal")));
    }

    @Test
    void givenNoResults_whenSearchQuery_thenReturnEmptyMap() throws Exception {
        HashMap<Long, String> results = new HashMap<>();
        given(service.searchCity("NoCitiesToMatch")).willReturn(results);

        mvc.perform(get("/api/search/NoCitiesToMatch").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    void whenSearchWithNoParameter_return404NotFound() throws Exception {
        mvc.perform(get("/api/search/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenExistingCity_andCityNotInCache_whenGetCityByName_returnCity() throws Exception {
        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", 6.0);
        airQualityValues.put("o3", 32.5);
        airQualityValues.put("pm10", 8.0);
        airQualityValues.put("pm25", 14.0);
        airQualityValues.put("so2", 1.5);
        CityData city = new CityData(
                8379L,
                "Entrecampos, Lisboa, Portugal",
                "2021-05-05 15:00:00",
                airQualityValues);
        given(service.getCityDataByName("Entrecampos, Lisboa, Portugal")).willReturn(city);
        given(cache.getCityByName("Entrecampos, Lisboa, Portugal")).willReturn(null);
        mvc.perform(get("/api/city/name/Entrecampos, Lisboa, Portugal").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx", is(city.getIdx().intValue())))
                .andExpect(jsonPath("$.name", is(city.getName())))
                .andExpect(jsonPath("$.timestamp", is(city.getTimestamp())))
                .andExpect(jsonPath("$.no2", is(city.getNo2())))
                .andExpect(jsonPath("$.o3", is(city.getO3())))
                .andExpect(jsonPath("$.pm10", is(city.getPm10())))
                .andExpect(jsonPath("$.pm25", is(city.getPm25())))
                .andExpect(jsonPath("$.so2", is(city.getSo2())));
        verify(service, VerificationModeFactory.times(1)).getCityDataByName(Mockito.anyString());
    }

    @Test
    void givenNonExistingCity_andCityNotInCache_whenGetCityByName_returnNotFound() throws Exception {
        given(service.getCityDataByName("No city")).willReturn(null);
        given(cache.getCityByName("No city")).willReturn(null);
        mvc.perform(get("/api/city/name/No city").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getCityDataByName(Mockito.anyString());
    }

    @Test
    void givenExistingCity_andCityInCache_whenGetCity_returnCity() throws Exception {
        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", 6.0);
        airQualityValues.put("o3", 32.5);
        airQualityValues.put("pm10", 8.0);
        airQualityValues.put("pm25", 14.0);
        airQualityValues.put("so2", 1.5);
        CityData city = new CityData(
                8379L,
                "Entrecampos, Lisboa, Portugal",
                "2021-05-05 15:00:00",
                airQualityValues);
        given(service.getCityDataByName("Entrecampos, Lisboa, Portugal")).willReturn(city);
        given(cache.getCityByName("Entrecampos, Lisboa, Portugal")).willReturn(city);
        mvc.perform(get("/api/city/name/Entrecampos, Lisboa, Portugal").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx", is(city.getIdx().intValue())))
                .andExpect(jsonPath("$.name", is(city.getName())))
                .andExpect(jsonPath("$.timestamp", is(city.getTimestamp())))
                .andExpect(jsonPath("$.no2", is(city.getNo2())))
                .andExpect(jsonPath("$.o3", is(city.getO3())))
                .andExpect(jsonPath("$.pm10", is(city.getPm10())))
                .andExpect(jsonPath("$.pm25", is(city.getPm25())))
                .andExpect(jsonPath("$.so2", is(city.getSo2())));
        verify(service, VerificationModeFactory.times(0)).getCityDataByName(Mockito.anyString());
    }



}
