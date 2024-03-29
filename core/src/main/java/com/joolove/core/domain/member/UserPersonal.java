package com.joolove.core.domain.member;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(catalog = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserPersonal extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_personal_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Boolean gatherAgree;    // 개인정보 수집 동의 여부

    private String phoneNumber;

    private String sex;

    private String birthday;

    private String country;

    @Builder
    public UserPersonal(UUID id, User user, Boolean gatherAgree, String phoneNumber, String sex, String birthday, String country) {
        this.id = id;
        this.user = user;
        this.gatherAgree = gatherAgree;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.birthday = birthday;
        this.country = country;
    }

    @Override
    public String toString() {
        return "UserPersonal{" +
                "id=" + id +
                ", user=" + user +
                ", gatherAgree=" + gatherAgree +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", country='" + country + '\'' +
                '}';
    }
}
