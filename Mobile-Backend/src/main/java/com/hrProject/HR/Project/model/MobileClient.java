package com.hrProject.HR.Project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name="mobile_client")
@Data
@AllArgsConstructor
public class MobileClient implements UserDetails{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="CLIENT_ID")
    private  Integer id;
    @Nonnull
    @Column(name = "EMAIL",unique = true)
    private String email;

    @Column(name = "ROLES")
    private List<String> roles;

    @Column(name = "SECRET")
    private String secret;

    @Column(name = "ENABLED")
    private boolean enabled;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID")
    private Company company;

    public MobileClient(){
        roles = new ArrayList<>();
        roles.add("USER");
        enabled = false ;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /*List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        for (String role : getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }*/
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
