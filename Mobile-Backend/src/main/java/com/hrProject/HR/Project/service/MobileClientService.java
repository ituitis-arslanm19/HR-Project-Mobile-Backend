package com.hrProject.HR.Project.service;

import com.hrProject.HR.Project.model.MobileClient;
import com.hrProject.HR.Project.repository.MobileClientRepository;
import org.springframework.stereotype.Service;

@Service
public class MobileClientService {
    private MobileClientRepository mobileClientRepository;

    public MobileClientService(MobileClientRepository mobileClientRepository) {
        this.mobileClientRepository = mobileClientRepository;

    }

    public MobileClient saveMobileClient(MobileClient mobileClient){
        return this.mobileClientRepository.save(mobileClient);
    }

    public MobileClient getMobileClientByEmail(String email){
        return this.mobileClientRepository.findByEmail(email).orElse(null);
    }


    public MobileClient getMobileClientById(Integer id){
        return this.mobileClientRepository.findById(id).orElse(null);
    }
}
