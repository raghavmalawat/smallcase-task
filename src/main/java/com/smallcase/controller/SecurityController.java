package com.smallcase.controller;

import com.smallcase.LogFactory;
import com.smallcase.dto.SecurityDTO;
import com.smallcase.services.SecurityService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/security")
public class SecurityController {

    private static final Logger logger = LogFactory.getLogger(SecurityController.class);

    @Autowired
    SecurityService securityService;

    @ApiOperation(value = "Add new security entry", response = SecurityDTO.class)
    @PostMapping(produces = "application/json")
    public SecurityDTO addSecurities(@RequestBody SecurityDTO securityDTO) {
        try {
            return securityService.addSecurity(securityDTO);
        } catch (Exception e) {
            logger.error("Error in adding a new security: {}", ExceptionUtils.getStackTrace(e));
            return SecurityDTO.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Retrieve all securities", response = SecurityDTO.class)
    @GetMapping(produces = "application/json")
    public SecurityDTO getSecurities(@RequestParam(value = "securityIds", required = false) List<Long> securityIds) {
        try {
            return securityService.getSecurities(securityIds);
        } catch (Exception e) {
            logger.error("Error in retrieving all securities: {}", ExceptionUtils.getStackTrace(e));
            return SecurityDTO.builder().message("Error").success(false).build();
        }
    }
}
