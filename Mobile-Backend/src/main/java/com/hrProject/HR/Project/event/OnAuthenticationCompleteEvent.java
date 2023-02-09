package com.hrProject.HR.Project.event;

import com.hrProject.HR.Project.model.MobileClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter

public class OnAuthenticationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private MobileClient mobileClient;
    private Locale locale;

    public OnAuthenticationCompleteEvent(final String appUrl, final MobileClient mobileClient, final Locale locale) {
        super(mobileClient);
        this.mobileClient = mobileClient;
        this.appUrl = appUrl;
        this.locale = locale;
    }


    // standard getters and setters
}
