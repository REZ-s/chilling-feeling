package com.joolove.core.domain.member;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.product.FavoriteGoods;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(catalog = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Favorite extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "favorite_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Favorite(UUID id, User user) {
        this.id = id;
        this.user = user;
    }

    /* mappedBy */
    @OneToMany(mappedBy = "favorite", cascade = CascadeType.PERSIST)
    private List<FavoriteGoods> favoriteGoodsList = new ArrayList<>();

    public void setFavoriteGoodsList(List<FavoriteGoods> favoriteGoodsList) {
        this.favoriteGoodsList = favoriteGoodsList;
    }

}
