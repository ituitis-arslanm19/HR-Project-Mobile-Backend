package com.hrProject.HR.Project.service;

import com.hrProject.HR.Project.event.OnAuthenticationCompleteEvent;
import com.hrProject.HR.Project.model.Company;
import com.hrProject.HR.Project.model.EmployeeRegisterDTO;
import com.hrProject.HR.Project.model.MobileClient;
import com.hrProject.HR.Project.model.MobileClientDTO;
import com.hrProject.HR.Project.security.JwtUtils;
import com.hrProject.HR.Project.utils.SecretGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class AuthenticationService {


    private final JwtUtils jwtUtils;

    private final ModelMapper modelMapper;

    private final VerificationTokenService verificationTokenService;

    private final MobileClientService mobileClientService;

    private final CompanyService companyService;

    private final SecretGenerator secretGenerator;

    private final ApplicationEventPublisher eventPublisher;


    @Value("${company.email.domain}")
    private String emailDomain;

    @Value("${company.server.address}")
    private String companyServer;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(JwtUtils jwtUtils, ModelMapper modelMapper, VerificationTokenService verificationTokenService, MobileClientService mobileClientService, CompanyService companyService, SecretGenerator secretGenerator, ApplicationEventPublisher eventPublisher) {
        this.jwtUtils = jwtUtils;
        this.modelMapper = modelMapper;
        this.verificationTokenService = verificationTokenService;
        this.mobileClientService = mobileClientService;
        this.companyService = companyService;
        this.secretGenerator = secretGenerator;
        this.eventPublisher = eventPublisher;
    }


    public MobileClientDTO registerNewMobileClient(EmployeeRegisterDTO employeeRegisterDto, HttpServletRequest request) {

//        if(emailExist(mobileClient.getEmail())){
//
//            //TODO: Eger email zaten kayıtlıysa
//
//        }
//
        Company company = isValidEmail(employeeRegisterDto.getEmail());
        if (company != null) {


            MobileClient registeredMobileClient;
            try {
                RestTemplate restTemplate = new RestTemplate();
                employeeRegisterDto.setSecret(secretGenerator.generate());
                registeredMobileClient = restTemplate.postForObject(company.getServerAddress()+"/employee", employeeRegisterDto, MobileClient.class);
                registeredMobileClient.setCompany(company);

                registeredMobileClient = mobileClientService.saveMobileClient(registeredMobileClient);

                eventPublisher.publishEvent(new OnAuthenticationCompleteEvent(request.getContextPath(), registeredMobileClient, request.getLocale()));
                return modelMapper.map(registeredMobileClient, MobileClientDTO.class);

            } catch (RestClientException e) {
                logger.error("Error while trying to communicate with the local server: " + e.getMessage());
            } catch (Exception e) {
                logger.error("Response from the local could not be handled:" + e.getMessage());
            }
        } else {
            logger.error("Email is not valid");
        }
        return null;
    }

    public MobileClientDTO login(String email, HttpServletRequest request) {


        Company company = isValidEmail(email);
        if (company != null) {
            try {
                MobileClient mobileClient = mobileClientService.getMobileClientByEmail(email);
                if (mobileClient != null) {
                    eventPublisher.publishEvent(new OnAuthenticationCompleteEvent(request.getContextPath(), mobileClient, request.getLocale()));

                    return modelMapper.map(mobileClient, MobileClientDTO.class);
                }
            } catch (Exception e) {
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

    public Company isValidEmail(String email) {
        String emailDomain = extractEmailDomain(email);

        if (emailDomain != null) {
            //             String regex = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(" + emailDomain + "))$";
            return  companyService.getByEmailDomain(emailDomain);
        }

        return null;

    }

    private String extractEmailDomain(String emailAddress) {
        int atIdx = emailAddress.indexOf("@");
        if (atIdx > 0) {
            return emailAddress.substring(atIdx + 1);
        }

        return null;
    }
}