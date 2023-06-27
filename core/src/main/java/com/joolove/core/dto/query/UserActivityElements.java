package com.joolove.core.dto.query;

import com.joolove.core.domain.EActivityCode;
import com.joolove.core.domain.ETargetCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserActivityElements {
    @NotNull
    private String username;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ETargetCode targetCode;

    @NotNull
    private String targetName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EActivityCode activityCode;

    private String activityDescription;

    @Builder
    public UserActivityElements(String username, ETargetCode targetCode, String targetName, EActivityCode activityCode, String activityDescription) {
        this.username = username;
        this.targetCode = targetCode;
        this.targetName = targetName;
        this.activityCode = activityCode;
        this.activityDescription = activityDescription;
    }

}
