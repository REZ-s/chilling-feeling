package com.joolove.core.controller;

import com.joolove.core.domain.billing.Cart;
import com.joolove.core.domain.billing.Orders;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.product.CartGoods;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.dto.query.GoodsView;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.request.CartRequest;
import com.joolove.core.dto.request.OrdersRequest;
import com.joolove.core.dto.request.SignInRequest;
import com.joolove.core.dto.response.CartResponse;
import com.joolove.core.repository.SocialLoginRepository;
import com.joolove.core.service.*;
import com.joolove.core.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
public class APIController {
    private final UserService userService;
    private final EmailServiceImpl emailService;
    private final SMSServiceImpl smsService;
    private final GoodsService goodsService;
    private final OrdersService ordersService;
    private final CartService cartService;
    private final SocialLoginRepository socialLoginRepository;
    private final PasswordUtils passwordUtils;

    @GetMapping("/api/v1/user/authentication")
    public ResponseEntity<?> checkAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.ok().body(authentication.getPrincipal());
        }

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/api/v1/authentication-code/email")
    public ResponseEntity<?> getAuthenticationCodeEmail(@Valid @RequestBody String email)
            throws Exception {
        String code = emailService.sendAuthCode(email);
        return ResponseEntity.ok().body(code);
    }

    @PostMapping("/api/v1/authentication-code/sms")
    public ResponseEntity<?> getAuthenticationCodeSMS(@Valid @RequestBody String phoneNumber)
            throws Exception {
        String code = smsService.sendAuthCode(phoneNumber);
        return ResponseEntity.ok().body(code);
    }

    @PostMapping("/api/v1/authentication-code/email/check")
    public ResponseEntity<?> checkAuthenticationCodeEmail(@Valid @RequestBody String code) {
        if (Objects.equals(emailService.getAuthCode(), code)) {
            return ResponseEntity.ok().body("valid");
        }
        return ResponseEntity.ok().body("invalid");
    }

    @PostMapping("/api/v1/authentication-code/sms/check")
    public ResponseEntity<?> checkAuthenticationCodeSMS(@Valid @RequestBody String code) {
        if (Objects.equals(smsService.getAuthCode(), code)) {
            return ResponseEntity.ok().body("valid");
        }
        return ResponseEntity.ok().body("invalid");
    }

    @PostMapping("/api/v1/email/check")
    public ResponseEntity<?> checkEmail(@Valid @RequestBody String email) {
        User user = userService.findByUsername(email);
        if (user == null) {
            return ResponseEntity.ok().body("invalid");
        }

        if (socialLoginRepository.existsByUser(user)) {
            return ResponseEntity.ok().body("valid-incorrect");
        }

        return ResponseEntity.ok().body("valid-correct");
    }

    @PostMapping("/api/v1/password/check")
    public ResponseEntity<?> checkPassword(@Valid @RequestBody SignInRequest request) {
        User user = userService.findByUsername(request.getUsername());
        String encodedPassword = user.getPassword().getPw();

        if (passwordUtils.matches(request.getPassword(), encodedPassword)) {
            return ResponseEntity.ok().body("valid");
        }

        return ResponseEntity.ok().body("invalid");
    }

    // 상품 검색 : 직접 검색하거나 카테고리를 선택할 때
    @GetMapping("/api/v1/goods")
    public ResponseEntity<List<IGoodsView>> getGoodsList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok().body(goodsService.findGoodsList(name, type, page, size, sort));
    }

    // 주문하기
    @PostMapping("/api/v1/order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrdersRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.ok().body("invalid user");
        }

        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body("invalid goods");
        }

        Orders order = ordersService.createOrder(user, goods, request.getGoodsCount());

        return ResponseEntity.ok().body("success");
    }

    // 베스트셀러 가져오기
    @GetMapping("/api/v1/goods/best-seller/{days}")
    public ResponseEntity<?> getBestSellerGoods(@Valid @PathVariable("days") Short days) {
        Map<String, Object> bestSellerWithSalesCount = ordersService.getBestSellerGoodsByDate(days);
        return ResponseEntity.ok().body(bestSellerWithSalesCount);
    }

    // 장바구니에 상품 저장
    @PostMapping("/api/v1/cart")
    public ResponseEntity<?> createCart(@Valid @RequestBody CartRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.ok().body("invalid user");
        }

        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body("invalid goods");
        }

        Cart cart = cartService.addCart(user, goods, request.getGoodsCount());

        return ResponseEntity.ok().body("success");
    }

    // 장바구니 불러오기
    @GetMapping("/api/v1/cart")
    public ResponseEntity<?> getCart(@Valid @RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.ok().body("invalid user");
        }

        List<CartGoods> cartGoodsList = cartService.getCart(user);
        List<String> goodsNameList = new ArrayList<>();
        Map<String, Integer> goodsNameWithCount = new HashMap<>();

        for (CartGoods c : cartGoodsList) {
            goodsNameList.add(c.getGoods().getName());
            goodsNameWithCount.put(c.getGoods().getName(), c.getCount());
        }

        List<IGoodsView> goodsViewList = goodsService.findGoodsListByGoodsName(goodsNameList);
        List<CartResponse> cartResponseList = new ArrayList<>();

        for (IGoodsView g : goodsViewList) {
            CartResponse cartResponse = CartResponse.builder()
                    .goodsView((GoodsView) g)
                    .goodsCount(goodsNameWithCount.get(g.getName()))
                    .build();

            cartResponseList.add(cartResponse);
        }

        if (cartResponseList.isEmpty()) {
            return ResponseEntity.ok().body(new ArrayList<>());
        }

        return ResponseEntity.ok().body(cartResponseList);
    }

    // 장바구니에서 상품 제거
    @DeleteMapping("/api/v1/cart")
    public ResponseEntity<?> removeCart(@Valid @RequestBody CartRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.ok().body("invalid user");
        }

        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body("invalid goods");
        }

        cartService.removeCart(user, goods);

        return ResponseEntity.ok().body("success");
    }
}
