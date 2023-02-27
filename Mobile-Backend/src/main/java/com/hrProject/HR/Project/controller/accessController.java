package com.hrProject.HR.Project.controller;

import com.hrProject.HR.Project.model.MobileClient;
import com.hrProject.HR.Project.model.QrCode;
import com.hrProject.HR.Project.service.AccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/access")
public class accessController {
    private final AccessService accessService;

    private static final Logger logger = LoggerFactory.getLogger(authenticationController.class);

    public accessController(AccessService accessService) {
        this.accessService = accessService;
    }


    @GetMapping
    public ResponseEntity<QrCode> generate(){
        String email = ((MobileClient)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        //logger.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        QrCode qrCode = this.accessService.generate(email);


        return ResponseEntity.ok(qrCode);
    }
}