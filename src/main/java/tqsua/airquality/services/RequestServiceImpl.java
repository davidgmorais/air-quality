package tqsua.airquality.services;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tqsua.airquality.configs.BasicHttpConnection;
import tqsua.airquality.models.CityData;
import java.util.HashMap;
import java.util.Map;

@Service
public class RequestServiceImpl implements RequestService{
    private static final String API_TOKEN = "d7f697b7ac4e2a3e9455ef49c19539d466b936f5";
    private static final String API_URL = "https://api.waqi.info/";
    private static final String STATUS_STRING = "status";
    private static final String ERROR_STRING = "error";

    @Autowired
    public BasicHttpConnection httpConnection;


    private Map<String, Double> buildAirQualityValues(JSONObject iaqi) {
        Double no2 = (iaqi.containsKey("no2")) ? Double.parseDouble(((JSONObject) iaqi.get("no2")).get("v").toString()) : null;
        Double o3 = (iaqi.containsKey("o3")) ? Double.parseDouble(((JSONObject) iaqi.get("o3")).get("v").toString()) : null;
        Double pm10 = (iaqi.containsKey("pm10")) ? Double.parseDouble(((JSONObject) iaqi.get("pm10")).get("v").toString()) : null;
        Double pm25 = (iaqi.containsKey("pm25")) ? Double.parseDouble(((JSONObject) iaqi.get("pm25")).get("v").toString()) : null;
        Double so2 = (iaqi.containsKey("so2")) ? Double.parseDouble(((JSONObject) iaqi.get("so2")).get("v").toString()) : null;

        Map<String, Double> airQualityValues = new HashMap<>();
        airQualityValues.put("no2", no2);
        airQualityValues.put("o3", o3);
        airQualityValues.put("pm10", pm10);
        airQualityValues.put("pm25", pm25);
        airQualityValues.put("so2", so2);
        return airQualityValues;
    }


    @Override
    public CityData getCityDataById(Long cityId) throws ParseException {
        String url = API_URL + "feed/@" + cityId.toString() + "/?token=" + API_TOKEN;
        String response = this.httpConnection.get(url);

        JSONObject obj = (JSONObject) new JSONParser().parse(response);
        obj = (JSONObject) obj.get("data");
        if (obj.containsKey(STATUS_STRING) && obj.get(STATUS_STRING).toString().equals(ERROR_STRING)) {
            return null;
        }

        Long idx = Long.parseLong(obj.get("idx").toString());
        String name = ((JSONObject) obj.get("city")).get("name").toString();
        String timestamp = ((JSONObject) obj.get("time")).get("s").toString();

        Map<String, Double> airQualityValues = this.buildAirQualityValues((JSONObject) obj.get("iaqi"));
        return new CityData(idx, name, timestamp, airQualityValues);
    }

    @Override
    public CityData getCityDataByName(String name) throws ParseException {
        String url = API_URL + "feed/" + name + "/?token=" + API_TOKEN;
        String response = this.httpConnection.get(url);

        JSONObject obj = (JSONObject) new JSONParser().parse(response);
        if (obj.get(STATUS_STRING).toString().equals(ERROR_STRING)) {
            return null;
        }

        obj = (JSONObject) obj.get("data");

        Long idx = Long.parseLong(obj.get("idx").toString());
        String timestamp = ((JSONObject) obj.get("time")).get("s").toString();
        String fullName = ((JSONObject) obj.get("city")).get("name").toString();

        Map<String, Double> airQualityValues = this.buildAirQualityValues((JSONObject) obj.get("iaqi"));
        return new CityData(idx, fullName, timestamp, airQualityValues);
    }

    @Override
    public HashMap<Long, String> searchCity(String query) throws ParseException {
        String url = API_URL + "search/?keyword=" + query + "&token=" + API_TOKEN;
        String response = this.httpConnection.get(url);

        JSONObject obj = (JSONObject) new JSONParser().parse(response);
        if (obj.get(STATUS_STRING).toString().equals(ERROR_STRING)) {
            return null;
        }

        JSONArray searchResults = (JSONArray) obj.get("data");
        HashMap<Long, String> results = new HashMap<>();

        for (Object res: searchResults) {
            JSONObject resJson = (JSONObject) res;
            results.put(Long.parseLong(resJson.get("uid").toString()), ((JSONObject) resJson.get("station")).get("name").toString());
        }

        return results;
    }
}
