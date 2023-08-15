package com.joolove.core.dto.query;

import com.joolove.core.domain.log.UserActivityLog;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserActivityLogElements {
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
    public UserActivityLogElements(String username, UserActivityLog.ETargetCode targetCode, String targetName, UserActivityLog.EActivityCode activityCode, String activityDescription) {
        this.username = username;
        this.targetCode = targetCode;
        this.targetName = targetName;
        this.activityCode = activityCode;
        this.activityDescription = activityDescription;
    }

}
