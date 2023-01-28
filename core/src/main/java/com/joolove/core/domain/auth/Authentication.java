package com.joolove.core.domain.auth;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Authentication extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "authentication_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Boolean gatherAgree;

    private String phoneNumber;

    @NotBlank
    @Email
    private String email;

    private String sex;

    private LocalDate birthday;

    private String country;

    @Builder
    public Authentication(UUID id, User user, Boolean gatherAgree, String phoneNumber, String email, String sex, LocalDate birthday, String country) {
        this.id = id;
        this.user = user;
        this.gatherAgree = gatherAgree;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.sex = sex;
        this.birthday = birthday;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "id=" + id +
                ", user=" + user +
                ", gatherAgree=" + gatherAgree +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", country='" + country + '\'' +
                '}';
    }
}
