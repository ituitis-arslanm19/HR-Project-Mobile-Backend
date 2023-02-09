package com.hrProject.HR.Project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String token;


    @ManyToOne (targetEntity = MobileClient.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "CLIENT_ID")
    private MobileClient mobileClient;

    private Date expiryDate;

    public VerificationToken(final String token, final MobileClient mobileClient) {
        super();

        this.token = token;
        this.mobileClient = mobileClient;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }



    // standard constructors, getters and setters
}