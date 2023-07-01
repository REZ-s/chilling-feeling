package com.joolove.core.domain.auth;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
import lombok.*;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Password extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "password_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @NotNull
    private String pw;

    @Builder
    public Password(UUID id, User user, String pw) {
        this.id = id;
        this.user = user;
        this.pw = pw;
    }

    @Override
    public String toString() {
        return "Password{" +
                "id=" + id +
                ", user=" + user +
                ", pw='" + pw + '\'' +
                '}';
    }
}
