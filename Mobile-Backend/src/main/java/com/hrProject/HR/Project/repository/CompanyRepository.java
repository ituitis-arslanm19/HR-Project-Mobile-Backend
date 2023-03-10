package com.hrProject.HR.Project.repository;


import com.hrProject.HR.Project.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {


    Optional<Company> findByEmailDomain(String EmailDomain);

}