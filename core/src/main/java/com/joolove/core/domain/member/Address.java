package com.joolove.core.domain.member;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(catalog = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "address_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Short addressType;

    @NotNull
    private String postalCode;

    @NotNull
    private String baseAddress;

    @NotNull
    private String detailAddress;

    @Builder
    public Address(UUID id, User user, Short addressType, String postalCode, String baseAddress, String detailAddress) {
        this.id = id;
        this.user = user;
        this.addressType = addressType;
        this.postalCode = postalCode;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", user=" + user +
                ", addressType=" + addressType +
                ", postalCode='" + postalCode + '\'' +
                ", baseAddress='" + baseAddress + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                '}';
    }

}
