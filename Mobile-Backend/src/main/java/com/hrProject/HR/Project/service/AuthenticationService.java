package com.hrProject.HR.Project.service;

import com.hrProject.HR.Project.enums.TokenStatus;
import com.hrProject.HR.Project.event.OnAuthenticationCompleteEvent;
import com.hrProject.HR.Project.model.EmployeeRegisterDto;
import com.hrProject.HR.Project.model.MobileClient;
import com.hrProject.HR.Project.security.JwtUtils;
import com.hrProject.HR.Project.utils.SecretGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class AuthenticationService {


    private final JwtUtils jwtUtils;

    private final VerificationTokenService verificationTokenService;

    private final MobileClientService mobileClientService;

    private final SecretGenerator secretGenerator;

    private final ApplicationEventPublisher eventPublisher;


    @Value("${company.email.domain}")
    private String emailDomain;

    @Value("${company.server.address}")
    private String companyServer;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(JwtUtils jwtUtils, VerificationTokenService verificationTokenService, MobileClientService mobileClientService, SecretGenerator secretGenerator, ApplicationEventPublisher eventPublisher) {
        this.jwtUtils = jwtUtils;
        this.verificationTokenService = verificationTokenService;
        this.mobileClientService = mobileClientService;
        this.secretGenerator = secretGenerator;
        this.eventPublisher = eventPublisher;
    }


    public MobileClient registerNewMobileClient(EmployeeRegisterDto employeeRegisterDto, HttpServletRequest request) {

//        if(emailExist(mobileClient.getEmail())){
//
//            //TODO: Eger email zaten kayıtlıysa
//
//        }
//
        if (isValidEmail(employeeRegisterDto.getEmail())) {


            MobileClient registeredMobileClient;
            try {
                RestTemplate restTemplate = new RestTemplate();
                employeeRegisterDto.setSecret(secretGenerator.generate());
                registeredMobileClient = restTemplate.postForObject(companyServer, employeeRegisterDto, MobileClient.class);
                registeredMobileClient = mobileClientService.saveMobileClient(registeredMobileClient);

                eventPublisher.publishEvent(new OnAuthenticationCompleteEvent(request.getContextPath(), registeredMobileClient, request.getLocale()));
                return registeredMobileClient;

            } catch (RestClientException e) {
                logger.error("Error while trying to communicate with the local server: " + e.getMessage());
            } catch (Exception e) {
                logger.error("Response from the local could not be handled:"+e.getMessage());
            }
        }else{
            logger.error("Email is not valid");
        }
        return null;
    }

    public MobileClient login(String email, HttpServletRequest request) {

        if (isValidEmail(email)) {
            try {
                MobileClient mobileClient = mobileClientService.getMobileClientByEmail(email);
                if(mobileClient!=null) {
                    eventPublisher.publishEvent(new OnAuthenticationCompleteEvent(request.getContextPath(), mobileClient, request.getLocale()));
                    return mobileClient;
                }
            }  catch (Exception e) {
                logger.error("Problem has occurred while logging in: " + e.getMessage());
            }
        }
        return null;

    }

    private boolean emailExist(String email) {
        return mobileClientService.getMobileClientByEmail(email) != null;
    }


    public MobileClient getMobileClient(String verificationToken) {
        MobileClient MobileClient = verificationTokenService.getVerificationToken(verificationToken).getMobileClient();
        return MobileClient;
    }

    public String createAccessToken(MobileClient mobileClient) {

        return jwtUtils.generateJwtToken(
                new UsernamePasswordAuthenticationToken(mobileClient, null, mobileClient.getAuthorities())
        );

    }

    public boolean isValidEmail(String email) {
        String regex = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(" + emailDomain + "))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();

    }
}