package com.hrProject.HR.Project.repository;

import com.hrProject.HR.Project.model.MobileClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MobileClientRepository extends JpaRepository<MobileClient, Integer> {

    Optional<MobileClient> findByEmail(String email);
    Optional<MobileClient> findById(Integer id);

}


