package com.smallcase.controller;

import com.smallcase.LogFactory;
import com.smallcase.dto.UserSecurityDTO;
import com.smallcase.services.UserSecurityService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/portfolio")
public class PortfolioController {

    private static final Logger logger = LogFactory.getLogger(PortfolioController.class);

    @Autowired
    UserSecurityService userSecurityService;

    @ApiOperation(value = "Retrieve user's current portfolio - All securities", response = UserSecurityDTO.class)
    @GetMapping(produces = "application/json")
    public UserSecurityDTO getUserPortfolio(@RequestParam(value = "userId") Long userId) {
        try {
            logger.info("Fetch user portfolio for userId: {}", userId);
            UserSecurityDTO response = userSecurityService.getUserSecurities(userId);
            logger.info("Fetch user portfolio response : {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error in fetching user portfolio: {}", ExceptionUtils.getStackTrace(e));
            return UserSecurityDTO.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Retrieve user's cumulative returns - All securities + Total", response = UserSecurityDTO.class)
    @GetMapping(value = "/returns", produces = "application/json")
    public UserSecurityDTO getUserPortfolioReturns(@RequestParam(value = "userId") Long userId) {
        try {
            logger.info("Fetch user portfolio returns for userId: {}", userId);
            UserSecurityDTO response = userSecurityService.getPortfolioReturns(userId);
            logger.info("Fetch user portfolio returns response : {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error in fetching user portfolio returns: {}", ExceptionUtils.getStackTrace(e));
            return UserSecurityDTO.builder().message("Error").success(false).build();
        }
    }
}
