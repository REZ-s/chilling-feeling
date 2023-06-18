package com.joolove.core.domain.log;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.EActivityCode;
import com.joolove.core.domain.member.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserActivityLog extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_activity_log_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private EActivityCode activityCode;

    private String activityDescription;

    @Builder
    public UserActivityLog(User user, EActivityCode activityCode, String activityDescription) {
        this.user = user;
        this.activityCode = activityCode;
        this.activityDescription = activityDescription;
    }

    @Override
    public String toString() {
        return "UserActivityLog{" +
                "id=" + id +
                ", activityCode=" + activityCode +
                ", activityDescription='" + activityDescription + '\'' +
                '}';
    }
}
