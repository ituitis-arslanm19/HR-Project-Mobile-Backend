package com.hrProject.HR.Project.listener;

import com.hrProject.HR.Project.controller.authenticationController;
import com.hrProject.HR.Project.event.OnAuthenticationCompleteEvent;
import com.hrProject.HR.Project.model.MobileClient;
import com.hrProject.HR.Project.service.MailService;
import com.hrProject.HR.Project.service.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticationListener implements
        ApplicationListener<OnAuthenticationCompleteEvent> {


    private final VerificationTokenService verificationTokenService;

    private final MessageSource messages;


    private final MailService mailService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationListener.class);


    public AuthenticationListener(VerificationTokenService verificationTokenService, MessageSource messages, MailService mailService) {
        this.verificationTokenService = verificationTokenService;
        this.messages = messages;
        this.mailService = mailService;
    }


    @Override
    public void onApplicationEvent(OnAuthenticationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnAuthenticationCompleteEvent event) {
        MobileClient mobileClient = event.getMobileClient();
        String token = UUID.randomUUID().toString();
        token = verificationTokenService.createVerificationToken(mobileClient, token).getToken();

        String recipientAddress = mobileClient.getEmail();
        String subject = "Onay kodunuz\n";
        String confirmationUrl
                = event.getAppUrl() + "/confirm?token=" + token;
        //String message = messages.getMessage("message.regSucc", null, event.getLocale());

        try {
            mailService.sendMessage(recipientAddress, subject, "\r\n" + "http://localhost:8080" + confirmationUrl);

            System.out.println("mail sent to " + recipientAddress);
        }
        catch (Exception e)
        {
            logger.error("Mail gönderilirken hata meydana geldi: "+e.getMessage());
        }

    }
}