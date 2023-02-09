package com.hrProject.HR.Project.utils.time;


import com.hrProject.HR.Project.exception.TimeProviderException;
import org.springframework.stereotype.Service;

import java.time.Instant;
@Service
public class TimeProvider {
    public long getTime() throws TimeProviderException {
        return Instant.now().getEpochSecond();
    }
}