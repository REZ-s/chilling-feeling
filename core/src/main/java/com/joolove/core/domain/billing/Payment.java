package com.joolove.core.domain.billing;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "billing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "payment_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Orders order;

    @NotNull
    private Integer rawPrice;

    @NotNull
    private Short discountRate;

    @NotNull
    private Integer point;

    @NotNull
    private Boolean isRefundable;

    @NotNull
    private Short paymentType;

    @NotNull
    private Short paymentStatus;

    @Builder
    public Payment(UUID id, Orders order, Integer rawPrice, Short discountRate, Integer point, Boolean isRefundable, Short paymentType, Short paymentStatus) {
        this.id = id;
        this.order = order;
        this.rawPrice = rawPrice;
        this.discountRate = discountRate;
        this.point = point;
        this.isRefundable = isRefundable;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", order=" + order +
                ", rawPrice=" + rawPrice +
                ", discountRate=" + discountRate +
                ", point=" + point +
                ", isRefundable=" + isRefundable +
                ", paymentType=" + paymentType +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
