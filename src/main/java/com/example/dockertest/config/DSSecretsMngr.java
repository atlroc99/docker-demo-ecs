package com.example.dockertest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DSSecretsMngr {

    @Autowired
    private DSConfig dsConfig;

    public void getSecret() {
        final String secretName = dsConfig.getSecretName();
        final String secretId = dsConfig.getSecretId();
        final String secretKey = dsConfig.getSecretKey();





    }

}
