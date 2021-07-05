package com.smallcase.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {
    @ApiOperation(value = "Service Health Check")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public String index() {
        return "Greetings from Spring Boot!. SmallCase Core Service is Up.";
    }
}
