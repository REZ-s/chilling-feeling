package com.joolove.core.domain.billing;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(catalog = "billing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Refund extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "refund_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Orders order;

    @NotNull
    private Short refundStatus;

    @NotNull
    private Boolean isRefundable;

    @NotNull
    private Integer refundPrice;

    @NotNull
    private Integer refundPoint;

    @Builder
    public Refund(UUID id, Orders order, Short refundStatus, Boolean isRefundable, Integer refundPrice, Integer refundPoint) {
        this.id = id;
        this.order = order;
        this.refundStatus = refundStatus;
        this.isRefundable = isRefundable;
        this.refundPrice = refundPrice;
        this.refundPoint = refundPoint;
    }

    @Override
    public String toString() {
        return "Refund{" +
                "id=" + id +
                ", order=" + order +
                ", refundStatus=" + refundStatus +
                ", isRefundable=" + isRefundable +
                ", refundPrice=" + refundPrice +
                ", refundPoint=" + refundPoint +
                '}';
    }

}
