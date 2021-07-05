package com.smallcase.controller;

import com.smallcase.dto.UserSecurityDTO;
import com.smallcase.services.UserSecurityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/portfolio")
public class PortfolioController {

    @Autowired
    UserSecurityService userSecurityService;

    @ApiOperation(value = "Retrieve user's current portfolio - All securities", response = UserSecurityDTO.class)
    @GetMapping(produces = "application/json")
    public UserSecurityDTO getUserPortfolio(@RequestParam(value = "userId") Long userId) {
        try {
            return userSecurityService.getUserSecurities(userId);
        } catch (Exception e) {
            return UserSecurityDTO.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Retrieve user's cumulative returns - All securities + Total", response = UserSecurityDTO.class)
    @GetMapping(value = "/returns", produces = "application/json")
    public UserSecurityDTO getUserPortfolioReturns(@RequestParam(value = "userId") Long userId) {
        try {
            return userSecurityService.getPortfolioReturns(userId);
        } catch (Exception e) {
            return UserSecurityDTO.builder().message("Error").success(false).build();
        }
    }
}
