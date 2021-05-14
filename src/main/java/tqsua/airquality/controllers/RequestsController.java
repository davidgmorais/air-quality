package tqsua.airquality.controllers;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqsua.airquality.cache.CacheManager;
import tqsua.airquality.models.CityData;
import tqsua.airquality.services.RequestServiceImpl;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RequestsController {

    @Autowired
    private RequestServiceImpl service;

    @Autowired
    private CacheManager cache;

    @GetMapping("/city/{idx}")
    public ResponseEntity<CityData> getCityById(@PathVariable(value = "idx") Long idx) throws ParseException {
        CityData city = this.cache.getCityById(idx);

        HttpStatus status = HttpStatus.OK;

        // city is not in cache
        if (city == null) {
            city = this.service.getCityDataById(idx);
            if (city != null) {
                this.cache.cacheCity(city);
            } else {
                status = HttpStatus.NOT_FOUND;
            }
        }

        return new ResponseEntity<>(city, status);
    }

    @GetMapping("/city/name/{name}")
    public ResponseEntity<CityData> getCityByName(@PathVariable(value = "name") String name) throws ParseException {
        CityData city = this.cache.getCityByName(name);

        HttpStatus status = HttpStatus.OK;

        // city is not in cache
        if (city == null) {
            city = this.service.getCityDataByName(name);
            if (city != null) {
                this.cache.cacheCity(city);
            } else {
                status = HttpStatus.NOT_FOUND;
            }
        }

        return new ResponseEntity<>(city, status);
    }

    @GetMapping("/search/{query}")
    public Map<Long, String> searchCityByName(@PathVariable(value = "query") String query) throws ParseException {
        return this.service.searchCity(query);
    }

    @GetMapping("/cache")
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        int requestTotal = this.cache.getRequestTotal();
        stats.put("RequestTotal", requestTotal);
        stats.put("Misses", this.cache.getMiss());
        stats.put("Hits", this.cache.getHit());
        stats.put("InCache", this.cache.getAllEntries());

        return stats;
    }


}
