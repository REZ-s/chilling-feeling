package com.joolove.core.domain.log;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.Device;
import com.joolove.core.domain.member.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(schema = "log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoginLog extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "login_log_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private UserActivityLog.EActivityCode activityCode;

    @NotNull
    private String ip;

    @NotNull
    private Short failCount;

    @NotNull
    private Short failReason;

    @Builder
    public LoginLog(UUID id, User user, Device device, UserActivityLog.EActivityCode activityCode, String ip, Short failCount, Short failReason) {
        this.id = id;
        this.user = user;
        this.device = device;
        this.activityCode = activityCode;
        this.ip = ip;
        this.failCount = failCount;
        this.failReason = failReason;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
                "id=" + id +
                ", user=" + user +
                ", device=" + device +
                ", activityCode=" + activityCode +
                ", ip='" + ip + '\'' +
                ", failCount=" + failCount +
                ", failReason=" + failReason +
                '}';
    }

}
