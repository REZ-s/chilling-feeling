package com.joolove.core.dto.query;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemoveUserActivityLogGoodsRequest {
    @NotNull
    private UUID deviceId;
    @NotNull
    private String targetName;

    @Builder
    public RemoveUserActivityLogGoodsRequest(UUID deviceId, String targetName) {
        this.deviceId = deviceId;
        this.targetName = targetName;
    }
}
