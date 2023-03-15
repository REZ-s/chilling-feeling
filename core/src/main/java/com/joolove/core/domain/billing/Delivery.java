package com.joolove.core.domain.billing;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
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
public class Delivery extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "delivery_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    @NotNull
    private Short deliveryStatus;

    @Builder
    public Delivery(UUID id, Orders order, Short deliveryStatus) {
        this.id = id;
        this.order = order;
        this.deliveryStatus = deliveryStatus;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", order=" + order +
                ", deliveryStatus=" + deliveryStatus +
                '}';
    }
}