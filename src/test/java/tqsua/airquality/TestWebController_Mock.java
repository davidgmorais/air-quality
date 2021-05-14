package tqsua.airquality;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AirQualityApplication.class)
@AutoConfigureMockMvc
class TestWebController_Mock {
    @Autowired
    private MockMvc mvc;

    @Test
    void whenGetIndexPage_thenReturn200() throws Exception {
       mvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void whenSearchOnIndexPage_thenReturn200() throws Exception{
        mvc.perform(get("/?search=Lisboa")).andExpect(status().isOk());
    }

    @Test
    void whenGetCity_thenReturn200() throws Exception {
        mvc.perform(get("/city/8379")).andExpect(status().isOk());
    }

    @Test
    void whenSearchOnCityPage_thenReturn200() throws Exception {
        mvc.perform(get("/city/8379?search=Madrid")).andExpect(status().isOk());
    }

    @Test
    void whenSearchEmptyStringOnCityPage_thenReturn200() throws Exception {
        mvc.perform(get("/city/8379?search=")).andExpect(status().isOk());
    }

    @Test
    void whenSearchEmptyStringOnIndexPage_thenReturn200() throws Exception {
        mvc.perform(get("/?search=")).andExpect(status().isOk());
    }

    @Test
    void whenGetCacheInfo_thenReturn200() throws Exception{
        mvc.perform(get("/cache")).andExpect(status().isOk());
    }

}
