package tqsua.airquality.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.parser.ParseException;
import tqsua.airquality.models.CityData;
import java.util.HashMap;

public interface RequestService {
    CityData getCityDataById(Long cityId) throws JsonProcessingException, ParseException;
    CityData getCityDataByName(String name) throws JsonProcessingException, ParseException;
    HashMap<Long, String> searchCity(String query) throws ParseException;

}
