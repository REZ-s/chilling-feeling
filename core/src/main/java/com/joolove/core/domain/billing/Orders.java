package com.joolove.core.domain.billing;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.product.OrdersGoods;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(catalog = "billing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Orders extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "order_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Short orderStatus;      // 주문 상태 (1: 주문중, 2: 주문완료, 3: 주문취소, 4: 주문실패)

    @Builder
    public Orders(UUID id, User user, Short orderStatus) {
        this.id = id;
        this.user = user;
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", user=" + user +
                ", orderStatus=" + orderStatus +
                ", refund=" + refund +
                ", delivery=" + delivery +
                ", payment=" + payment +
                ", ordersGoodsList=" + ordersGoodsList +
                '}';
    }

    /* mappedBy */
    @OneToOne(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Refund refund;

    @OneToOne(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Delivery delivery;

    @OneToOne(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Payment payment;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrdersGoods> ordersGoodsList = new ArrayList<>();

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public void setOrdersGoods(List<OrdersGoods> ordersGoodsList) {
        this.ordersGoodsList = ordersGoodsList;
    }

    public void updateOrderStatus(short status) {
        this.orderStatus = status;
    }

}
