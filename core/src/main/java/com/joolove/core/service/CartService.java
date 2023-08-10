package com.joolove.core.service;

import com.joolove.core.domain.billing.Cart;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.product.CartGoods;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.repository.CartGoodsRepository;
import com.joolove.core.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartGoodsRepository cartGoodsRepository;

    // 장바구니 생성 (비어있음)
    @Transactional
    public Cart createCart(User user) {
        Cart cart = Cart.builder()
                .user(user)
                .build();
        cartRepository.save(cart);
        return cart;
    }

    // 장바구니에 상품 저장
    @Transactional
    public Cart addCart(User user, Goods goods, int inputCount) {
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = createCart(user);
        }

        List<CartGoods> cartGoodsList = cart.getCartGoodsList();

        // 장바구니에 상품이 하나도 없는 경우
        if (cartGoodsList == null) {
            cartGoodsList = new ArrayList<>();
        }

        boolean isExist = false;

        // 장바구니에 이미 같은 상품이 존재하는 경우
        for (CartGoods cartGoods : cartGoodsList) {
            if (cartGoods.getGoods().getId() == goods.getId()) {
                isExist = true;
                cartGoods.updateCount(cartGoods.getCount() + inputCount);
            }
        }

        // 장바구니에 해당 상품이 없는 경우
        if (!isExist) {
            CartGoods cartGoods = CartGoods.builder()
                    .goods(goods)
                    .cart(cart)
                    .count(inputCount)
                    .build();
            cartGoodsRepository.save(cartGoods);
            cartGoodsList.add(cartGoods);
        }

        cart.setCartGoodsList(cartGoodsList);
        return cart;
    }

    // 장바구니에서 상품 삭제
    @Transactional
    public void removeCart(User user, Goods goods) {
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = createCart(user);
        }

        List<CartGoods> cartGoodsList = cart.getCartGoodsList();
        for (CartGoods c : cartGoodsList) {
            if (c.getGoods().getId() == goods.getId()) {
                cartGoodsList.remove(c);
                cartGoodsRepository.deleteById(c.getId());
                break;
            }
        }

        cart.setCartGoodsList(cartGoodsList);
        cartRepository.save(cart);
    }

    // 장바구니 불러오기
    public List<CartGoods> getCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = createCart(user);
        }

        return cartGoodsRepository.findByCartId(cart.getId());
    }
}
