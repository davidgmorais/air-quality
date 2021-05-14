package tqsua.airquality.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;

@Configuration
public class BasicHttpConnection {
    private final RestTemplate restTemplate;

    public BasicHttpConnection() {
        this.restTemplate = new RestTemplate();
    }

    public String get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        // to get result as String
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}
