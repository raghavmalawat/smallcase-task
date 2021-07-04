package com.smallcase.controller;

import com.smallcase.dto.UserSecurityDTO;
import com.smallcase.services.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    @Autowired
    UserSecurityService userSecurityService;

    @GetMapping(produces = "application/json")
    public UserSecurityDTO getUserPortfolio(@RequestParam(value = "userId") Long userId) {
        try {
            return userSecurityService.getUserSecurities(userId);
        } catch (Exception e) {
            return UserSecurityDTO.builder().message("Error").success(false).build();
        }
    }

    @GetMapping(value = "/returns", produces = "application/json")
    public UserSecurityDTO getUserPortfolioReturns(@RequestParam(value = "userId") Long userId) {
        try {
            return userSecurityService.getPortfolioReturns(userId);
        } catch (Exception e) {
            return UserSecurityDTO.builder().message("Error").success(false).build();
        }
    }
}
