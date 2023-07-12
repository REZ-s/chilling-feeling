package com.joolove.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class EnvironmentController {
    private final Environment env;

    @GetMapping("/environment/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> liveProfiles = Arrays.asList("live1", "live2");
        String defaultProfile = profiles.isEmpty() ? "live" : profiles.get(0);

        return profiles.stream()
                .filter(liveProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }

    @GetMapping("/environment/port")
    public String port() {
        String port = env.getProperty("server.port");
        return port == null || port.isEmpty() || port.equals("0") ? "8081" : port;
    }
}