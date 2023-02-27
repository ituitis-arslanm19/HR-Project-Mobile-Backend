package com.hrProject.HR.Project.service;

import com.hrProject.HR.Project.exception.CodeGenerationException;
import com.hrProject.HR.Project.model.MobileClient;
import com.hrProject.HR.Project.model.QrCode;
import com.hrProject.HR.Project.totp.CodeGenerator;
import com.hrProject.HR.Project.utils.time.TimeProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessService {
    private final MobileClientService MobileClientService;

    private final TimeProvider timeProvider;
    private final CodeGenerator codeGenerator;
    private int timePeriod = 30;

    public AccessService(MobileClientService mobileClientService,  TimeProvider timeProvider, CodeGenerator codeGenerator) {
        this.MobileClientService = mobileClientService;

        this.timeProvider = timeProvider;
        this.codeGenerator = codeGenerator;
    }



    public QrCode generate(String email) {
        QrCode qrCode;
        try{
            MobileClient employee = this.MobileClientService.getMobileClientByEmail(email);
            long counter = Math.floorDiv(timeProvider.getTime(), timePeriod);
            qrCode =new QrCode();
            qrCode.setData(this.codeGenerator.generate(employee.getSecret(), counter)+","+email );
            return qrCode;
        }catch (CodeGenerationException e){
            return null;
        }
    }
}