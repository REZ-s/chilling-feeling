package com.joolove.core.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String username;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;

    @NotBlank
    @Size(min = 10, max = 20)
    private String phoneNumber;

    private List<String> roles;

    @Builder
    public SignUpRequest(String username, String password, String phoneNumber, List<String> roles) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    public static SignUpRequest buildEmpty() {
        return SignUpRequest.builder()
                .username("")
                .password("")
                .phoneNumber("")
                .build();
    }
}