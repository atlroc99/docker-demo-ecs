package com.example.dockertest.controller;

import com.example.dockertest.service.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class DemoController {

    private final ImageService imageService;

    public DemoController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public String test() {
        return "200 Success";
    }

    @GetMapping("/test")
    public String test2() {
        return "200 SUCCESS hello..working";
    }

    @GetMapping("/env/option")
    public String getEvnOption() {
        return "ENV OPTION: " + imageService.printEnvOption();
    }

    @GetMapping("/env/options")
    public List<String> getEnvOptions() {
        return imageService.getEnvOptions();
    }

}
// build and push docker image to docker hub via mvn docker plugin
//mvn clean package -Drevision=<versionNO> -Ddockerfile.useMavenSettingsForAuth=true dockerfile:push
//docker run --name spring-boot-app -d -p 8090:8080 72611cd385af --name=Mohammad --age=23 --salary=1000000