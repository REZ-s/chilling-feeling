package com.joolove.core.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInResponse {
    private UUID id;
    private String username;
    private String role;

    @Builder
    public SignInResponse(UUID id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}