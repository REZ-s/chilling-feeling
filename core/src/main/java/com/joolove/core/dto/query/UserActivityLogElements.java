package com.joolove.core.dto.query;

import com.joolove.core.domain.log.UserActivityLog;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserActivityLogElements {
    @NotNull
    private UUID deviceId;

    @NotNull
    private String username;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserActivityLog.ETargetCode targetCode;

    @NotNull
    private String targetName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserActivityLog.EActivityCode activityCode;

    private String activityDescription;

    @Builder
    public UserActivityLogElements(UUID deviceId, String username, UserActivityLog.ETargetCode targetCode, String targetName, UserActivityLog.EActivityCode activityCode, String activityDescription) {
        this.deviceId = deviceId;
        this.username = username;
        this.targetCode = targetCode;
        this.targetName = targetName;
        this.activityCode = activityCode;
        this.activityDescription = activityDescription;
    }

}
