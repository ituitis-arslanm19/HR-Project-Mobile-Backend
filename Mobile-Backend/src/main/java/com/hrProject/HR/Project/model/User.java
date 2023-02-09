/*
package com.hrProject.HR.Project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
@Entity

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer  id;
    private String username;
    private String password;
    private List<String> roles;

    @OneToOne
    @JoinColumn(name="EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID")
    private Employee employee;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        for (String role : getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public String getUsername() {
        // TODO Auto-generated method stub
        return this.username;
    }

    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}*/
