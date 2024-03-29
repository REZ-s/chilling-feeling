package com.joolove.core.domain.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
public class LogoutToken extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "logout_token_id", columnDefinition = "BINARY(16)")
    private UUID id;

    private String username;

    @NotBlank
    @Column(unique = true)
    private String token;

    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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

