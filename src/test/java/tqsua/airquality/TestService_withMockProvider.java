package tqsua.airquality;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqsua.airquality.configs.BasicHttpConnection;
import tqsua.airquality.models.CityData;
import tqsua.airquality.services.RequestServiceImpl;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TestService_withMockProvider {
    @Mock
    BasicHttpConnection httpConnection;

    @InjectMocks
    RequestServiceImpl requestService;

    @BeforeEach
    void setUp() {}

    @Test
    void whenGetCityById_andCityExistsInApi_returnCityData() throws ParseException {
        Mockito.when(httpConnection.get("https://api.waqi.info/feed/@8379/?token=d7f697b7ac4e2a3e9455ef49c19539d466b936f5"))
                .thenReturn("{\"status\":\"ok\",\"data\":{\"aqi\":33,\"idx\":8379,\"attributions\":[{\"url\":\"http://qualar.apambiente.pt/\",\"name\":\"Portugal -Agencia Portuguesa do Ambiente - Qualidade do Ar\",\"logo\":\"portugal-qualar.png\"},{\"url\":\"http://www.eea.europa.eu/themes/air/\",\"name\":\"European Environment Agency\",\"logo\":\"Europe-EEA.png\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[38.748611111111,-9.1488888888889],\"name\":\"Entrecampos, Lisboa, Portugal\",\"url\":\"https://aqicn.org/city/portugal/lisboa/entrecampos\"},\"dominentpol\":\"o3\",\"iaqi\":{\"dew\":{\"v\":12},\"h\":{\"v\":54.5},\"no2\":{\"v\":6},\"o3\":{\"v\":32.5},\"p\":{\"v\":1017},\"pm10\":{\"v\":8},\"pm25\":{\"v\":14},\"so2\":{\"v\":1.5},\"t\":{\"v\":21.5},\"w\":{\"v\":7.7},\"wg\":{\"v\":12.3}},\"time\":{\"s\":\"2021-05-05 15:00:00\",\"tz\":\"+01:00\",\"v\":1620226800,\"iso\":\"2021-05-05T15:00:00+01:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":32,\"day\":\"2021-05-05\",\"max\":36,\"min\":29},{\"avg\":29,\"day\":\"2021-05-06\",\"max\":34,\"min\":24},{\"avg\":25,\"day\":\"2021-05-07\",\"max\":34,\"min\":19},{\"avg\":27,\"day\":\"2021-05-08\",\"max\":35,\"min\":20},{\"avg\":22,\"day\":\"2021-05-09\",\"max\":23,\"min\":22}],\"pm10\":[{\"avg\":8,\"day\":\"2021-05-05\",\"max\":9,\"min\":6},{\"avg\":7,\"day\":\"2021-05-06\",\"max\":8,\"min\":6},{\"avg\":9,\"day\":\"2021-05-07\",\"max\":11,\"min\":7},{\"avg\":10,\"day\":\"2021-05-08\",\"max\":13,\"min\":7},{\"avg\":9,\"day\":\"2021-05-09\",\"max\":9,\"min\":8}],\"pm25\":[{\"avg\":23,\"day\":\"2021-05-05\",\"max\":25,\"min\":19},{\"avg\":25,\"day\":\"2021-05-06\",\"max\":29,\"min\":18},{\"avg\":30,\"day\":\"2021-05-07\",\"max\":38,\"min\":22},{\"avg\":33,\"day\":\"2021-05-08\",\"max\":43,\"min\":23},{\"avg\":36,\"day\":\"2021-05-09\",\"max\":36,\"min\":33}],\"uvi\":[{\"avg\":1,\"day\":\"2021-05-05\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-06\",\"max\":7,\"min\":0},{\"avg\":1,\"day\":\"2021-05-07\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-08\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-09\",\"max\":6,\"min\":0},{\"avg\":0,\"day\":\"2021-05-10\",\"max\":0,\"min\":0}]}},\"debug\":{\"sync\":\"2021-05-06T01:50:35+09:00\"}}}");
        CityData result = requestService.getCityDataById(8379L);
        Map<String, Double> assertionValues = new HashMap<>();
        assertionValues.put("no2", 6.0);
        assertionValues.put("o3", 32.5);
        assertionValues.put("pm10", 8.0);
        assertionValues.put("pm25", 14.0);
        assertionValues.put("so2", 1.5);
        CityData assertCityData = new CityData(
                8379L,
                "Entrecampos, Lisboa, Portugal",
                "2021-05-05 15:00:00",
                assertionValues);
        assertEquals(result, assertCityData);
    }

    @Test
    void whenGetCityById_andCityDoesNotExistInApi_returnNull() throws ParseException{
        Mockito.when(httpConnection.get("https://api.waqi.info/feed/@456571872638172/?token=d7f697b7ac4e2a3e9455ef49c19539d466b936f5"))
                .thenReturn("{\"status\":\"ok\",\"data\":{\"status\":\"error\",\"msg\":\"Unknown ID\"}}");
        CityData result = requestService.getCityDataById(456571872638172L);
        assertNull(result);
    }

    @Test
    void whenGetCityByName_andCityExistsInApi_returnCityData() throws ParseException {
        Mockito.when(httpConnection.get("https://api.waqi.info/feed/Lisbon/?token=d7f697b7ac4e2a3e9455ef49c19539d466b936f5"))
                .thenReturn("{\"status\":\"ok\",\"data\":{\"aqi\":33,\"idx\":8379,\"attributions\":[{\"url\":\"http://qualar.apambiente.pt/\",\"name\":\"Portugal -Agencia Portuguesa do Ambiente - Qualidade do Ar\",\"logo\":\"portugal-qualar.png\"},{\"url\":\"http://www.eea.europa.eu/themes/air/\",\"name\":\"European Environment Agency\",\"logo\":\"Europe-EEA.png\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[38.748611111111,-9.1488888888889],\"name\":\"Entrecampos, Lisboa, Portugal\",\"url\":\"https://aqicn.org/city/portugal/lisboa/entrecampos\"},\"dominentpol\":\"o3\",\"iaqi\":{\"dew\":{\"v\":12},\"h\":{\"v\":54.5},\"no2\":{\"v\":6},\"o3\":{\"v\":32.5},\"p\":{\"v\":1017},\"pm10\":{\"v\":8},\"pm25\":{\"v\":14},\"so2\":{\"v\":1.5},\"t\":{\"v\":21.5},\"w\":{\"v\":7.7},\"wg\":{\"v\":12.3}},\"time\":{\"s\":\"2021-05-05 15:00:00\",\"tz\":\"+01:00\",\"v\":1620226800,\"iso\":\"2021-05-05T15:00:00+01:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":32,\"day\":\"2021-05-05\",\"max\":36,\"min\":29},{\"avg\":29,\"day\":\"2021-05-06\",\"max\":34,\"min\":24},{\"avg\":25,\"day\":\"2021-05-07\",\"max\":34,\"min\":19},{\"avg\":27,\"day\":\"2021-05-08\",\"max\":35,\"min\":20},{\"avg\":22,\"day\":\"2021-05-09\",\"max\":23,\"min\":22}],\"pm10\":[{\"avg\":8,\"day\":\"2021-05-05\",\"max\":9,\"min\":6},{\"avg\":7,\"day\":\"2021-05-06\",\"max\":8,\"min\":6},{\"avg\":9,\"day\":\"2021-05-07\",\"max\":11,\"min\":7},{\"avg\":10,\"day\":\"2021-05-08\",\"max\":13,\"min\":7},{\"avg\":9,\"day\":\"2021-05-09\",\"max\":9,\"min\":8}],\"pm25\":[{\"avg\":23,\"day\":\"2021-05-05\",\"max\":25,\"min\":19},{\"avg\":25,\"day\":\"2021-05-06\",\"max\":29,\"min\":18},{\"avg\":30,\"day\":\"2021-05-07\",\"max\":38,\"min\":22},{\"avg\":33,\"day\":\"2021-05-08\",\"max\":43,\"min\":23},{\"avg\":36,\"day\":\"2021-05-09\",\"max\":36,\"min\":33}],\"uvi\":[{\"avg\":1,\"day\":\"2021-05-05\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-06\",\"max\":7,\"min\":0},{\"avg\":1,\"day\":\"2021-05-07\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-08\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-09\",\"max\":6,\"min\":0},{\"avg\":0,\"day\":\"2021-05-10\",\"max\":0,\"min\":0}]}},\"debug\":{\"sync\":\"2021-05-06T01:50:35+09:00\"}}}");
        CityData result = requestService.getCityDataByName("Lisbon");
        Map<String, Double> assertionValues = new HashMap<>();
        assertionValues.put("no2", 6.0);
        assertionValues.put("o3", 32.5);
        assertionValues.put("pm10", 8.0);
        assertionValues.put("pm25", 14.0);
        assertionValues.put("so2", 1.5);
        CityData assertCityData = new CityData(
                8379L,
                "Entrecampos, Lisboa, Portugal",
                "2021-05-05 15:00:00",
                assertionValues);
        assertEquals(result, assertCityData);
    }

    @Test
    void whenGetCityByName_andCityDoesNotExistInApi_returnNull() throws ParseException{
        Mockito.when(httpConnection.get("https://api.waqi.info/feed/randomcountry/?token=d7f697b7ac4e2a3e9455ef49c19539d466b936f5"))
                .thenReturn("{\"status\":\"error\",\"data\":\"Unknown station\"}");
        CityData result = requestService.getCityDataByName("randomcountry");
        assertNull(result);
    }

    @Test
    void whenSearchCity_andThereAreResults_returnArray() throws ParseException {
        Mockito.when(httpConnection.get("https://api.waqi.info/search/?keyword=Lisbon&token=d7f697b7ac4e2a3e9455ef49c19539d466b936f5"))
                .thenReturn("{\"status\":\"ok\",\"data\":[{\"uid\":8379,\"aqi\":\"25\",\"time\":{\"tz\":\"+01:00\",\"stime\":\"2021-05-06 10:00:00\",\"vtime\":1620291600},\"station\":{\"name\":\"Entrecampos, Lisboa, Portugal\",\"geo\":[38.748611111111,-9.1488888888889],\"url\":\"portugal/lisboa/entrecampos\",\"country\":\"PT\"}},{\"uid\":10513,\"aqi\":\"24\",\"time\":{\"tz\":\"+01:00\",\"stime\":\"2021-05-06 10:00:00\",\"vtime\":1620291600},\"station\":{\"name\":\"Olivais, Lisboa, Portugal\",\"geo\":[38.768888888889,-9.1080555555556],\"url\":\"portugal/lisboa/olivais\",\"country\":\"PT\"}},{\"uid\":8381,\"aqi\":\"24\",\"time\":{\"tz\":\"+01:00\",\"stime\":\"2021-05-06 10:00:00\",\"vtime\":1620291600},\"station\":{\"name\":\"Laranjeiro, Almada, Portugal\",\"geo\":[38.663611111111,-9.1577777777778],\"url\":\"portugal/almada/laranjeiro\",\"country\":\"PT\"}}]}");
        HashMap<Long, String> result = requestService.searchCity("Lisbon");
        assertEquals(3, result.size());
        assertTrue(result.containsKey(8379L) && result.get(8379L).equals("Entrecampos, Lisboa, Portugal"));
        assertTrue(result.containsKey(10513L) && result.get(10513L).equals("Olivais, Lisboa, Portugal"));
        assertTrue(result.containsKey(8381L) && result.get(8381L).equals("Laranjeiro, Almada, Portugal"));
    }

    @Test
    void whenSearchCity_andThereAreNoResults_returnEmptyHashMap() throws ParseException {
        Mockito.when(httpConnection.get("https://api.waqi.info/search/?keyword=Viseu&token=d7f697b7ac4e2a3e9455ef49c19539d466b936f5"))
                .thenReturn("{\"status\":\"ok\",\"data\":[]}");
        HashMap<Long, String> result = requestService.searchCity("Viseu");
        assertTrue(result.isEmpty());
    }

    @Test
    void whenSearchCity_andApiError_returnNull() throws ParseException {
        Mockito.when(httpConnection.get("https://api.waqi.info/search/?keyword=Viseu&token=d7f697b7ac4e2a3e9455ef49c19539d466b936f5"))
                .thenReturn("{\"status\":\"error\",\"data\":\"Invalid key\"}");
        HashMap<Long, String> result = requestService.searchCity("Viseu");
        assertNull(result);
    }

    @Test
    void whenGetCityById_andEmptyFieldsExists_returnCityDataWithNullFields() throws ParseException {
        Mockito.when(httpConnection.get("https://api.waqi.info/feed/@8379/?token=d7f697b7ac4e2a3e9455ef49c19539d466b936f5"))
                .thenReturn("{\"status\":\"ok\",\"data\":{\"aqi\":33,\"idx\":8379,\"attributions\":[{\"url\":\"http://qualar.apambiente.pt/\",\"name\":\"Portugal -Agencia Portuguesa do Ambiente - Qualidade do Ar\",\"logo\":\"portugal-qualar.png\"},{\"url\":\"http://www.eea.europa.eu/themes/air/\",\"name\":\"European Environment Agency\",\"logo\":\"Europe-EEA.png\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[38.748611111111,-9.1488888888889],\"name\":\"Entrecampos, Lisboa, Portugal\",\"url\":\"https://aqicn.org/city/portugal/lisboa/entrecampos\"},\"dominentpol\":\"o3\",\"iaqi\":{},\"time\":{\"s\":\"2021-05-05 15:00:00\",\"tz\":\"+01:00\",\"v\":1620226800,\"iso\":\"2021-05-05T15:00:00+01:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":32,\"day\":\"2021-05-05\",\"max\":36,\"min\":29},{\"avg\":29,\"day\":\"2021-05-06\",\"max\":34,\"min\":24},{\"avg\":25,\"day\":\"2021-05-07\",\"max\":34,\"min\":19},{\"avg\":27,\"day\":\"2021-05-08\",\"max\":35,\"min\":20},{\"avg\":22,\"day\":\"2021-05-09\",\"max\":23,\"min\":22}],\"pm10\":[{\"avg\":8,\"day\":\"2021-05-05\",\"max\":9,\"min\":6},{\"avg\":7,\"day\":\"2021-05-06\",\"max\":8,\"min\":6},{\"avg\":9,\"day\":\"2021-05-07\",\"max\":11,\"min\":7},{\"avg\":10,\"day\":\"2021-05-08\",\"max\":13,\"min\":7},{\"avg\":9,\"day\":\"2021-05-09\",\"max\":9,\"min\":8}],\"pm25\":[{\"avg\":23,\"day\":\"2021-05-05\",\"max\":25,\"min\":19},{\"avg\":25,\"day\":\"2021-05-06\",\"max\":29,\"min\":18},{\"avg\":30,\"day\":\"2021-05-07\",\"max\":38,\"min\":22},{\"avg\":33,\"day\":\"2021-05-08\",\"max\":43,\"min\":23},{\"avg\":36,\"day\":\"2021-05-09\",\"max\":36,\"min\":33}],\"uvi\":[{\"avg\":1,\"day\":\"2021-05-05\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-06\",\"max\":7,\"min\":0},{\"avg\":1,\"day\":\"2021-05-07\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-08\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-09\",\"max\":6,\"min\":0},{\"avg\":0,\"day\":\"2021-05-10\",\"max\":0,\"min\":0}]}},\"debug\":{\"sync\":\"2021-05-06T01:50:35+09:00\"}}}");
        CityData result = requestService.getCityDataById(8379L);
        CityData assertCityData = new CityData(
                8379L,
                "Entrecampos, Lisboa, Portugal",
                "2021-05-05 15:00:00",
                null);
        assertEquals(result, assertCityData);
    }

    @Test
    void whenGetCityByName_andEmptyFieldsExists_returnCityDataWithNullFields() throws ParseException {
        Mockito.when(httpConnection.get("https://api.waqi.info/feed/Lisbon/?token=d7f697b7ac4e2a3e9455ef49c19539d466b936f5"))
                .thenReturn("{\"status\":\"ok\",\"data\":{\"aqi\":33,\"idx\":8379,\"attributions\":[{\"url\":\"http://qualar.apambiente.pt/\",\"name\":\"Portugal -Agencia Portuguesa do Ambiente - Qualidade do Ar\",\"logo\":\"portugal-qualar.png\"},{\"url\":\"http://www.eea.europa.eu/themes/air/\",\"name\":\"European Environment Agency\",\"logo\":\"Europe-EEA.png\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[38.748611111111,-9.1488888888889],\"name\":\"Entrecampos, Lisboa, Portugal\",\"url\":\"https://aqicn.org/city/portugal/lisboa/entrecampos\"},\"dominentpol\":\"o3\",\"iaqi\":{},\"time\":{\"s\":\"2021-05-05 15:00:00\",\"tz\":\"+01:00\",\"v\":1620226800,\"iso\":\"2021-05-05T15:00:00+01:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":32,\"day\":\"2021-05-05\",\"max\":36,\"min\":29},{\"avg\":29,\"day\":\"2021-05-06\",\"max\":34,\"min\":24},{\"avg\":25,\"day\":\"2021-05-07\",\"max\":34,\"min\":19},{\"avg\":27,\"day\":\"2021-05-08\",\"max\":35,\"min\":20},{\"avg\":22,\"day\":\"2021-05-09\",\"max\":23,\"min\":22}],\"pm10\":[{\"avg\":8,\"day\":\"2021-05-05\",\"max\":9,\"min\":6},{\"avg\":7,\"day\":\"2021-05-06\",\"max\":8,\"min\":6},{\"avg\":9,\"day\":\"2021-05-07\",\"max\":11,\"min\":7},{\"avg\":10,\"day\":\"2021-05-08\",\"max\":13,\"min\":7},{\"avg\":9,\"day\":\"2021-05-09\",\"max\":9,\"min\":8}],\"pm25\":[{\"avg\":23,\"day\":\"2021-05-05\",\"max\":25,\"min\":19},{\"avg\":25,\"day\":\"2021-05-06\",\"max\":29,\"min\":18},{\"avg\":30,\"day\":\"2021-05-07\",\"max\":38,\"min\":22},{\"avg\":33,\"day\":\"2021-05-08\",\"max\":43,\"min\":23},{\"avg\":36,\"day\":\"2021-05-09\",\"max\":36,\"min\":33}],\"uvi\":[{\"avg\":1,\"day\":\"2021-05-05\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-06\",\"max\":7,\"min\":0},{\"avg\":1,\"day\":\"2021-05-07\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-08\",\"max\":8,\"min\":0},{\"avg\":1,\"day\":\"2021-05-09\",\"max\":6,\"min\":0},{\"avg\":0,\"day\":\"2021-05-10\",\"max\":0,\"min\":0}]}},\"debug\":{\"sync\":\"2021-05-06T01:50:35+09:00\"}}}");
        CityData result = requestService.getCityDataByName("Lisbon");
        CityData assertCityData = new CityData(
                8379L,
                "Entrecampos, Lisboa, Portugal",
                "2021-05-05 15:00:00",
                null);
        assertEquals(result, assertCityData);
    }

}
