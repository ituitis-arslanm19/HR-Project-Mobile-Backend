package com.hrProject.HR.Project.service;


import com.hrProject.HR.Project.repository.MobileClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    final private MobileClientRepository mobileClientRepository;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {



        return  mobileClientRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));



    }}

