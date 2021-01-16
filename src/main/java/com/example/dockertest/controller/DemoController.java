package com.example.dockertest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DemoController {

    @GetMapping
    public String test() {
        return "200 Success";
    }

    @GetMapping("/test")
    public String test2() {
        return "hello..working";
    }

    @GetMapping("/updated-image")
    public String test3() {
        return "200 success from test 3....";
    }

    //mvn clean package -Drevision=<versionNO> -Ddockerfile.useMavenSettingsForAuth=true dockerfile:push
}
