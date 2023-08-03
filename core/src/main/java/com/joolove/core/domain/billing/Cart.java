package com.joolove.core.domain.billing;

import com.joolove.core.domain.member.User;
import com.joolove.core.domain.product.CartGoods;
import com.joolove.core.domain.product.FavoriteGoods;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class Cart {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "cart_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Cart(UUID id, User user) {
        this.id = id;
        this.user = user;
    }

    /* mappedBy */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
    private List<CartGoods> cartGoodsList = new ArrayList<>();

    public void setCartGoodsList(List<CartGoods> cartGoodsList) {
        this.cartGoodsList = cartGoodsList;
    }
}