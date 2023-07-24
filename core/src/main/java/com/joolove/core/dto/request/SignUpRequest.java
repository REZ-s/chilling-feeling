package com.joolove.core.dto.request;

import com.joolove.core.domain.auth.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private String phoneNumber;

    @Builder
    public SignUpRequest(String username, String password, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public static SignUpRequest buildEmpty() {
        return SignUpRequest.builder()
                .username("")
                .password("")
                .phoneNumber("")
                .build();
    }
}