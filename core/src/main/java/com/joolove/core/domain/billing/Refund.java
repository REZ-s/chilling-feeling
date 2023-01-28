package com.joolove.core.domain.billing;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "billing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Refund {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "refund_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
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

    @NotNull
    private LocalDateTime created;

    @NotNull
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @Builder
    public Refund(UUID id, Orders order, Short refundStatus, Boolean isRefundable, Integer refundPrice, Integer refundPoint, LocalDateTime created, LocalDateTime lastUpdate) {
        this.id = id;
        this.order = order;
        this.refundStatus = refundStatus;
        this.isRefundable = isRefundable;
        this.refundPrice = refundPrice;
        this.refundPoint = refundPoint;
        this.created = created;
        this.lastUpdate = lastUpdate;
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
                ", created=" + created +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

}
