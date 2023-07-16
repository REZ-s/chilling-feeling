package com.joolove.core.domain.auth;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash(value = "LogoutToken", timeToLive = 3600)
public class LogoutToken extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "logout_token_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Indexed
    private String username;

    @Indexed
    @NotBlank
    @Column(unique = true)
    private String token;

    @NotNull
    @TimeToLive
    private LocalDateTime expiryDate;

    @Builder
    public LogoutToken(UUID id, String username, String token, LocalDateTime expiryDate) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public static LocalDateTime setExpiryDate(RefreshToken refreshToken) {
        LocalDateTime endedDate = refreshToken.getExpiryDate();
        LocalDateTime createdDate = refreshToken.getCreatedDate();

        int surplus = endedDate.getSecond() - createdDate.getSecond();
        return LocalDateTime.now().plusSeconds(surplus);
    }

    public static LocalDateTime setExpiryDate(LocalDateTime endedDate, LocalDateTime createdDate) {
        int surplus = endedDate.getSecond() - createdDate.getSecond();
        return LocalDateTime.now().plusSeconds(surplus);
    }

}

