package com.joolove.core.domain.auth;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(schema = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CIDI extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "cidi_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String ci;

    @NotNull
    private String di;

    @Builder
    public CIDI(UUID id, User user, String ci, String di) {
        this.id = id;
        this.user = user;
        this.ci = ci;
        this.di = di;
    }

    @Override
    public String toString() {
        return "CIDI{" +
                "id=" + id +
                ", user=" + user +
                ", ci='" + ci + '\'' +
                ", di='" + di + '\'' +
                '}';
    }

}
