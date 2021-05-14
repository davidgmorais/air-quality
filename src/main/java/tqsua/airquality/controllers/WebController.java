package tqsua.airquality.controllers;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tqsua.airquality.models.CityData;
import java.util.Map;

@Controller
public class WebController {
    @Autowired
    RequestsController requestsController;

    @GetMapping(path = "/")
    public String index( @RequestParam(name="search", required=false) String searchQuery,
    Model model) throws ParseException {
        if (searchQuery != null && !searchQuery.equals("")) {
            Map<Long, String> searchResults = this.requestsController.searchCityByName(searchQuery);
            model.addAttribute("searchResults", searchResults);
        }
        return "index";
    }


    @GetMapping(path = "/cache")
    public String cache(Model model) {
        Map<String, Object> stats = this.requestsController.getCacheStats();
        model.addAllAttributes(stats);
        return "cache";
    }

    @GetMapping(path = "/city/{idx}")
    public String getCityById(
            @PathVariable(value = "idx") Long idx,
            @RequestParam(name="search", required=false) String searchQuery,
            Model model) throws ParseException {

        if (searchQuery != null && !searchQuery.equals("")) {
            Map<Long, String> searchResults = this.requestsController.searchCityByName(searchQuery);
            model.addAttribute("searchResults", searchResults);
        }

        ResponseEntity<CityData> requested = this.requestsController.getCityById(idx);
        model.addAttribute("city", requested.getBody());
        return "cityInfo";
    }
}
