package com.joolove.core.controller;

import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.product.CartGoods;
import com.joolove.core.domain.product.FavoriteGoods;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.dto.query.GoodsView;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.RemoveUserActivityLogGoodsRequest;
import com.joolove.core.dto.query.UserActivityLogElements;
import com.joolove.core.dto.request.CartRequest;
import com.joolove.core.dto.request.OrdersRequest;
import com.joolove.core.dto.request.SignInRequest;
import com.joolove.core.dto.request.FavoriteRequest;
import com.joolove.core.dto.response.CartResponse;
import com.joolove.core.dto.response.FavoriteResponse;
import com.joolove.core.repository.SocialLoginRepository;
import com.joolove.core.service.*;
import com.joolove.core.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    private final FavoriteService favoriteService;
    private final SocialLoginRepository socialLoginRepository;
    private final PasswordUtils passwordUtils;
    private final UserActivityLogService userActivityLogService;

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

        ordersService.createOrder(user, goods, request.getGoodsCount());

        return ResponseEntity.ok().body("success");
    }

    // 베스트셀러 가져오기
    @GetMapping("/api/v1/goods/best-seller/{days}")
    public ResponseEntity<?> getBestSellerGoods(@Valid @PathVariable("days") Short days) {
        Map<String, Object> bestSellerWithSalesCount = ordersService.getBestSellerGoodsByDate(days);
        return ResponseEntity.ok().body(bestSellerWithSalesCount);
    }

    // 인기상품 리스트 불러오기
    @GetMapping("/api/v1/goods/popular/{days}")
    public ResponseEntity<?> getPopularGoodsList(@Valid @PathVariable("days") Short days) {
        List<IGoodsView> popularGoodsList = userActivityLogService.findBestViewsUserActivityList(0, 10, days);
        return ResponseEntity.ok().body(popularGoodsList);
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

        cartService.addCart(user, goods, request.getGoodsCount());

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

        for (CartGoods cg : cartGoodsList) {
            goodsNameList.add(cg.getGoods().getName());
            goodsNameWithCount.put(cg.getGoods().getName(), cg.getCount());
        }

        List<IGoodsView> goodsViewList = goodsService.findGoodsListByGoodsName(goodsNameList);
        List<CartResponse> cartResponseList = new ArrayList<>();

        for (IGoodsView gv : goodsViewList) {
            CartResponse cartResponse = CartResponse.builder()
                    .goodsView((GoodsView) gv)
                    .goodsCount(goodsNameWithCount.get(gv.getName()))
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

    // 위시리스트에 상품 저장
    @PostMapping("/api/v1/wishlist")
    public ResponseEntity<?> createWishList(@Valid @RequestBody FavoriteRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.ok().body("invalid user");
        }

        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body("invalid goods");
        }

        favoriteService.addWishList(user, goods);

        return ResponseEntity.ok().body("success");
    }

    // 위시리스트 불러오기
    @GetMapping("/api/v1/wishlist")
    public ResponseEntity<?> getWishList(@Valid @RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.ok().body("invalid user");
        }

        List<FavoriteGoods> favoriteGoodsList = favoriteService.getWishList(user);
        List<String> goodsNameList = new ArrayList<>();

        for (FavoriteGoods fg : favoriteGoodsList) {
            goodsNameList.add(fg.getGoods().getName());
        }

        List<IGoodsView> goodsViewList = goodsService.findGoodsListByGoodsName(goodsNameList);
        List<FavoriteResponse> favoriteResponseList = new ArrayList<>();

        for (IGoodsView gv : goodsViewList) {
            FavoriteResponse favoriteResponse = FavoriteResponse.builder()
                    .goodsView((GoodsView) gv)
                    .build();

            favoriteResponseList.add(favoriteResponse);
        }

        if (favoriteResponseList.isEmpty()) {
            return ResponseEntity.ok().body(new ArrayList<>());
        }

        return ResponseEntity.ok().body(favoriteResponseList);
    }

    // 위시리스트에서 상품 제거
    @DeleteMapping("/api/v1/wishlist")
    public ResponseEntity<?> removeWishList(@Valid @RequestBody FavoriteRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.ok().body("invalid user");
        }

        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body("invalid goods");
        }

        favoriteService.removeWishList(user, goods);

        return ResponseEntity.ok().body("success");
    }

    // 이미 위시리스트에 있는 상품인지 확인
    @PostMapping("/api/v1/wishlist/checked")
    public ResponseEntity<?> checkWishListGoods(@Valid @RequestBody FavoriteRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.ok().body(false);
        }

        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body(false);
        }

        List<FavoriteGoods> favoriteGoodsList = favoriteService.getWishList(user);

        for (FavoriteGoods fg : favoriteGoodsList) {
            if (fg.getGoods().getName().equals(request.getGoodsName())) {
                return ResponseEntity.ok().body(true);
            }
        }

        return ResponseEntity.ok().body(false);
    }

    // device id 생성
    @GetMapping("/api/v1/device/id")
    public ResponseEntity<?> getDeviceId() {
        return ResponseEntity.ok().body(UUID.randomUUID());
    }

    // 사용자 행동 로그 불러오기 (상품)
    @GetMapping("/api/v1/recommendation/log/activity/goods")
    @ResponseBody
    public ResponseEntity<Object> getRecommendationActivityLogGoods(@Valid @RequestParam UUID deviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.badRequest().build();
        }

        List<UserActivityLogElements> userActivityLogElements = userActivityLogService.findUserActivityListByDeviceIdAndTargetCode(deviceId, UserActivityLog.ETargetCode.GOODS);
        List<String> targetNameList = new ArrayList<>();
        for (UserActivityLogElements ual : userActivityLogElements) {
            targetNameList.add(ual.getTargetName());
        }

        return ResponseEntity.ok().body(targetNameList);
    }

    // 사용자 행동 로그 제거 (상품)
    @DeleteMapping("/api/v1/recommendation/log/activity/goods")
    @ResponseBody
    public ResponseEntity<Object> removeRecommendationActivityLogGoods(@Valid @RequestBody RemoveUserActivityLogGoodsRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.badRequest().build();
        }

        userActivityLogService.removeUserActivityLog(request.getDeviceId(), request.getTargetName(), UserActivityLog.ETargetCode.GOODS);
        return ResponseEntity.ok().build();
    }

}
