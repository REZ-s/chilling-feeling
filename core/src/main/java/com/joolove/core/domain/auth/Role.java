package com.joolove.core.domain.auth;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(catalog = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Role extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "role_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ERole name = ERole.ROLE_USER;

    @Builder
    public Role(UUID id, User user, ERole name) {
        this.id = id;
        this.user = user;
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum ERole {
        ROLE_USER,
        ROLE_MANAGER,
        ROLE_ADMIN
    }

}