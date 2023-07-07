package com.joolove.core.domain.member;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.log.LoginLog;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Device extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "device_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Short inUse;

    @NotNull
    private Integer registerNo;

    @NotNull
    private UUID uuid;

    @NotNull
    private String model;

    @NotNull
    private Integer osType;

    @NotNull
    private Integer osVersion;

    @Builder
    public Device(UUID id, User user, Short inUse, Integer registerNo, UUID uuid, String model, Integer osType, Integer osVersion) {
        this.id = id;
        this.user = user;
        this.inUse = inUse;
        this.registerNo = registerNo;
        this.uuid = uuid;
        this.model = model;
        this.osType = osType;
        this.osVersion = osVersion;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", user=" + user +
                ", inUse=" + inUse +
                ", registerNo=" + registerNo +
                ", uuid=" + uuid +
                ", model='" + model + '\'' +
                ", osType=" + osType +
                ", osVersion=" + osVersion +
                '}';
    }

    /* mappedBy */
    @OneToMany(mappedBy = "device",  fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<LoginLog> loginLogs = new ArrayList<>();

    public void setLoginLogs(List<LoginLog> loginLogs) {
        this.loginLogs = loginLogs;
    }

}
