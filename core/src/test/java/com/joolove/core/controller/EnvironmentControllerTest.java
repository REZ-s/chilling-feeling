package com.joolove.core.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EnvironmentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void port_인증없이_호출() {
        String expected = "8081";

        ResponseEntity<String> response = restTemplate.getForEntity("/environment/port", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void profile_인증없이_호출() {
        String expected = "default";

        ResponseEntity<String> response = restTemplate.getForEntity("/environment/profile", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void port_조회() {
        //given
        String expectedPort = "8081";
        MockEnvironment env = new MockEnvironment();
        env.setProperty("server.port", expectedPort);

        EnvironmentController controller = new EnvironmentController(env);

        //when
        String port = controller.port();

        //then
        assertThat(port).isEqualTo(expectedPort);
    }

    @Test
    public void real_profile_조회() {
        //given
        String expectedProfile = "real";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        EnvironmentController controller = new EnvironmentController(env);

        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void real_profile_없으면_첫번째가_조회() {
        //given
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");

        EnvironmentController controller = new EnvironmentController(env);

        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void active_profile_없으면_default_조회() {
        //given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        EnvironmentController controller = new EnvironmentController(env);

        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}