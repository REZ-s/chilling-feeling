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
    private Integer rawPrice;       // 원가

    @NotNull
    private Short discountRate;     // 할인율

    @NotNull
    private Integer point;          // 마일리지

    @NotNull
    private Boolean isRefundable;   // 환불 가능 여부

    @NotNull
    private Short paymentType;      // 결제 타입 (1: Web, 2: IOS, 3: Android)

    @NotNull
    private Short paymentStatus;    // 결제 상태 (1: 결제중, 2: 결제완료, 3: 결제취소, 4: 결제실패)

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

    public void updatePaymentStatus(short status) {
        if (status > 4 || status < 0) {
            return;
        }

        this.paymentStatus = status;
    }
}
