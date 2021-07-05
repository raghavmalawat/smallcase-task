package com.smallcase.controller;

import com.smallcase.dto.SecurityDTO;
import com.smallcase.services.SecurityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/security")
public class SecurityController {

    @Autowired
    SecurityService securityService;

    @ApiOperation(value = "Add new security entry", response = SecurityDTO.class)
    @PostMapping(produces = "application/json")
    public SecurityDTO addSecurities(@RequestBody SecurityDTO securityDTO) {
        try {
            return securityService.addSecurity(securityDTO);
        } catch (Exception e) {
            return SecurityDTO.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Retrieve all securities", response = SecurityDTO.class)
    @GetMapping(produces = "application/json")
    public SecurityDTO getSecurities(@RequestParam(value = "securityIds", required = false) List<Long> securityIds) {
        try {
            return securityService.getSecurities(securityIds);
        } catch (Exception e) {
            return SecurityDTO.builder().message("Error").success(false).build();
        }
    }
}
