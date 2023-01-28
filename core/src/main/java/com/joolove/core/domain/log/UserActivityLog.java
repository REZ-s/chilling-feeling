package com.joolove.core.domain.log;

import com.joolove.core.domain.BaseTimeStamp;
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
    private String tableName;

    @NotNull
    private Integer rowId;

    @NotNull
    private String activityCode;

    @NotNull
    private String asIs;

    @NotNull
    private String toBe;

    @NotNull
    private Short modifier;

    @Builder
    public UserActivityLog(UUID id, User user, String tableName, Integer rowId, String activityCode, String asIs, String toBe, Short modifier) {
        this.id = id;
        this.user = user;
        this.tableName = tableName;
        this.rowId = rowId;
        this.activityCode = activityCode;
        this.asIs = asIs;
        this.toBe = toBe;
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return "UserActivityLog{" +
                "id=" + id +
                ", user=" + user +
                ", tableName='" + tableName + '\'' +
                ", rowId=" + rowId +
                ", activityCode='" + activityCode + '\'' +
                ", asIs='" + asIs + '\'' +
                ", toBe='" + toBe + '\'' +
                ", modifier=" + modifier +
                '}';
    }

}
