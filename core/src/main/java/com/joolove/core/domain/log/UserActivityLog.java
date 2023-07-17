package com.joolove.core.domain.log;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(catalog = "log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserActivityLog extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_activity_log_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EActivityCode activityCode;     // 활동 코드 (예: 로그인, 로그아웃, 회원가입, 검색, 클릭 등)

    private String activityDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ETargetCode targetCode;         // 활동 목적 대상 (예: 상품을 클릭했다면, Goods)

    private String targetName;

    @Builder
    public UserActivityLog(User user, EActivityCode activityCode, String activityDescription, ETargetCode targetCode, String targetName) {
        this.user = user;
        this.activityCode = activityCode;
        this.activityDescription = activityDescription;
        this.targetCode = targetCode;
        this.targetName = targetName;
    }

    @Override
    public String toString() {
        return "UserActivityLog{" +
                "id=" + id +
                ", activityCode=" + activityCode +
                ", activityDescription='" + activityDescription + '\'' +
                ", targetCode='" + targetCode + '\'' +
                ", targetName='" + targetName + '\'' +
                '}';
    }

    public enum EActivityCode {
        LOGIN,
        LOGOUT,
        SIGNUP,
        SEARCH,
        CLICK,
        UNDEFINED
    }

    public enum ETargetCode {
        GOODS,
        USER,
        CATEGORY,
        UNKNOWN
    }
}
