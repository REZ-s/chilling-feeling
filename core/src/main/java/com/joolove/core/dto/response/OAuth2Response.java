package com.joolove.core.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Response {
    private String email;
    private String name;
    private String picture;

    @Builder
    public OAuth2Response(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public static OAuth2Response build(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return OAuth2Response.builder()
                .email((String)attributes.get("email"))
                .name((String)attributes.get("name"))
                .picture((String)attributes.get("picture"))
                .build();
    }
}