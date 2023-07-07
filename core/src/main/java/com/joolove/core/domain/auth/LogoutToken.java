package com.joolove.core.domain.auth;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
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

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Column(unique = true)
    private String token;

    @Builder
    public LogoutToken(UUID id, User user, String token) {
        this.id = id;
        this.user = user;
        this.token = token;
    }

    @Override
    public String toString() {
        return "LogoutToken{" +
                "id=" + id +
                ", user=" + user +
                ", token='" + token + '\'' +
                '}';
    }

}

