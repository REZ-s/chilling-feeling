package com.joolove.core.domain.auth;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(schema = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SocialLogin extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "social_login_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @NotNull
    private Short providerCode;   // 0:noting, 1:google, 2:naver, 3:kakao, ...

    @NotBlank
    @Column(unique = true)
    private String providerId;  // Authorization Server ID

    @Column(unique = true)
    private String accessToken; // oauth 2.0 jwt

    @Builder
    public SocialLogin(UUID id, User user, Short providerCode, String providerId, String accessToken) {
        this.id = id;
        this.user = user;
        this.providerCode = providerCode;
        this.providerId = providerId;
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "SocialLogin{" +
                "id=" + id +
                ", user=" + user +
                ", providerCode=" + providerCode +
                ", providerId='" + providerId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }

    public static Short convertToProviderCode(String provider) {
        return switch (provider) {
            case "google" -> 1;
            case "naver" -> 2;
            case "kakao" -> 3;
            default -> 0;
        };
    }

    public static @Nullable String convertToProvider(Short providerCode) {
        return switch (providerCode) {
            case 1 -> "google";
            case 2 -> "naver";
            case 3 -> "kakao";
            default -> null;
        };
    }
}
