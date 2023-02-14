package com.hrProject.HR.Project.service;

import com.hrProject.HR.Project.model.Company;
import com.hrProject.HR.Project.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);


    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }



    public Company getByEmailDomain(String emailDomain) {


        return companyRepository.findByEmailDomain(emailDomain).orElse(null);
    }
}
