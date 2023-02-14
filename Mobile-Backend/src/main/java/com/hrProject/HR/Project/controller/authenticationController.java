package com.hrProject.HR.Project.controller;

import com.hrProject.HR.Project.enums.TokenStatus;
import com.hrProject.HR.Project.model.EmployeeRegisterDTO;
import com.hrProject.HR.Project.model.MobileClient;
import com.hrProject.HR.Project.model.MobileClientDTO;
import com.hrProject.HR.Project.model.VerificationToken;
import com.hrProject.HR.Project.service.MobileClientService;
import com.hrProject.HR.Project.service.AuthenticationService;
import com.hrProject.HR.Project.service.VerificationTokenService;
import com.hrProject.HR.Project.utils.SecretGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class authenticationController {

    final private AuthenticationService authenticationService;
    final private MobileClientService mobileClientService;

    final private VerificationTokenService verificationTokenService;


    private static final Logger logger = LoggerFactory.getLogger(authenticationController.class);


    public authenticationController(ApplicationEventPublisher eventPublisher, AuthenticationService authenticationService, MobileClientService mobileClientService, SecretGenerator secretGenerator, VerificationTokenService verificationTokenService) {
        this.authenticationService = authenticationService;
        this.mobileClientService = mobileClientService;
        this.verificationTokenService = verificationTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<MobileClientDTO> registerMobileClient(
            @RequestBody EmployeeRegisterDTO employeeRegisterDto
            , HttpServletRequest request) {

        return ResponseEntity.ok(authenticationService.registerNewMobileClient(employeeRegisterDto, request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmAuthentication
            (HttpServletRequest request, @RequestParam("token") String token) {

        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        TokenStatus tokenStatus = verificationTokenService.validateVerificationToken(verificationToken);

        if (tokenStatus == TokenStatus.VALID) {
            MobileClient mobileClient = verificationToken.getMobileClient();

            mobileClient.setEnabled(true);

            return ResponseEntity.ok(authenticationService.createAccessToken(mobileClientService.saveMobileClient(mobileClient)));
        }

        if (tokenStatus == TokenStatus.EMPTY) {
            logger.error("Verification token is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (tokenStatus == TokenStatus.EXPIRED) {
            logger.error("Verification token is expired.");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Your verification token has expired");

        }

        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<MobileClientDTO> login(
            @RequestBody String email
            , HttpServletRequest request) {

        return ResponseEntity.ok(authenticationService.login(email, request));
    }


}
