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
@RedisHash(value = "RefreshToken", timeToLive = 3600)
public class RefreshToken extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "refresh_token_id", columnDefinition = "BINARY(16)")
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
    public RefreshToken(UUID id, String username, String token, LocalDateTime expiryDate) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.expiryDate = expiryDate;
    }

}