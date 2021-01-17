package com.example.dockertest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ImageService {

    private final Environment environment;

    public ImageService(Environment environment) {
        this.environment = environment;
    }

    public String printEnvOption() {
        String name = environment.getProperty("name");
        return name;
    }

    public List<String> getEnvOptions() {
        return Arrays.asList(environment.getProperty("name"),
                environment.getProperty("age"),
                environment.getProperty("salary"));
    }
}
