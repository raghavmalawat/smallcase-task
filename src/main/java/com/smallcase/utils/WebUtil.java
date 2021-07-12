package com.smallcase.utils;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WebUtil {
     Gson gson = new Gson();
    public Object getCurrentPrice(List<String> tickerSymbols) {
        String uri = "https://quotes-api.tickertape.in/quotes";

        uri = uri + "sids?" + String.join(",", tickerSymbols);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return gson.toJson(result);
    }
}
