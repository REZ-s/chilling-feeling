package com.joolove.core.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SigninResponse {
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;

    @Builder
    public SigninResponse(UUID id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}