package com.smallcase.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String index() {
        return "Greetings from Spring Boot!. SmallCase Core Service is Up.";
    }
}
