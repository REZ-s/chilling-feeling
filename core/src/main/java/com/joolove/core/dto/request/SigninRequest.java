package com.joolove.core.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SigninRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String username;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;

    @Builder
    public SigninRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static SigninRequest buildEmpty() {
        return SigninRequest.builder()
                .username("")
                .password("")
                .build();
    }
}