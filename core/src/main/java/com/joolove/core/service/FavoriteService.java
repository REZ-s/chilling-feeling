package com.joolove.core.service;

import com.joolove.core.domain.member.Favorite;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.product.FavoriteGoods;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.repository.FavoriteGoodsRepository;
import com.joolove.core.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteGoodsRepository favoriteGoodsRepository;

    // 위시리스트 생성 (비어있음)
    @Transactional
    public Favorite createWishList(User user) {
        Favorite favorite = Favorite.builder()
                .user(user)
                .build();
        return favoriteRepository.save(favorite);
    }

    // 위시리스트에 상품 저장
    @Transactional
    public Favorite addWishList(User user, Goods goods) {
        Favorite favorite = favoriteRepository.findByUserId(user.getId());

        if (favorite == null) {
            favorite = createWishList(user);
        }

        List<FavoriteGoods> favoriteGoodsList = favorite.getFavoriteGoodsList();

        // 위시리스트에 상품이 하나도 없는 경우
        if (favoriteGoodsList == null) {
            favoriteGoodsList = new ArrayList<>();
        }

        boolean isExist = false;

        // 위시리스트에 이미 같은 상품이 존재하는 경우
        for (FavoriteGoods favoriteGoods : favoriteGoodsList) {
            if (favoriteGoods.getGoods().getId() == goods.getId()) {
                isExist = true;
            }
        }

        // 위시리스트에 해당 상품이 없는 경우
        if (!isExist) {
            FavoriteGoods favoriteGoods = FavoriteGoods.builder()
                    .goods(goods)
                    .favorite(favorite)
                    .build();
            favoriteGoodsRepository.save(favoriteGoods);
            favoriteGoodsList.add(favoriteGoods);
        }

        favorite.setFavoriteGoodsList(favoriteGoodsList);
        return favorite;
    }

    // 위시리스트에서 상품 삭제
    @Transactional
    public void removeWishList(User user, Goods goods) {
        Favorite favorite = favoriteRepository.findByUserId(user.getId());

        if (favorite == null) {
            favorite = createWishList(user);
        }

        List<FavoriteGoods> favoriteGoodsList = favorite.getFavoriteGoodsList();
        for (FavoriteGoods fg : favoriteGoodsList) {
            if (fg.getGoods().getId() == goods.getId()) {
                favoriteGoodsList.remove(fg);
                favoriteGoodsRepository.deleteById(fg.getId());
                break;
            }
        }

        favorite.setFavoriteGoodsList(favoriteGoodsList);
        favoriteRepository.save(favorite);
    }

    // 위시리스트 불러오기
    public List<FavoriteGoods> getWishList(User user) {
        Favorite favorite = favoriteRepository.findByUserId(user.getId());

        if (favorite == null) {
            favorite = createWishList(user);
        }

        return favoriteGoodsRepository.findByFavoriteId(favorite.getId());
    }
}
