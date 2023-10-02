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
import com.joolove.core.dto.request.*;
import com.joolove.core.dto.response.CartResponse;
import com.joolove.core.dto.response.FavoriteResponse;
import com.joolove.core.model.RestAPIResponse;
import com.joolove.core.repository.SocialLoginRepository;
import com.joolove.core.service.*;
import com.joolove.core.utils.PasswordUtils;
import com.joolove.core.utils.RecommendationUtils;
import com.joolove.core.utils.aop.APILoginState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ExecutionException;

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
    private final UserActivityLogService userActivityLogService;
    private final SocialLoginRepository socialLoginRepository;
    private final PasswordUtils passwordUtils;
    private final RecommendationUtils recommendationUtils;

    @GetMapping("/api/v1/user/authentication")
    public ResponseEntity<?> checkAuthenticatedUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.ok().body(RestAPIResponse.success(authentication.getPrincipal()));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success(null));
    }

    @PostMapping("/api/v1/authentication-code/email")
    public ResponseEntity<?> getAuthenticationCodeEmail(@Valid @RequestBody String email) throws Exception {
        String code = emailService.sendAuthCode(email);
        return ResponseEntity.ok().body(RestAPIResponse.success(code));
    }

    @PostMapping("/api/v1/authentication-code/sms")
    public ResponseEntity<?> getAuthenticationCodeSMS(@Valid @RequestBody String phoneNumber) throws Exception {
        String code = smsService.sendAuthCode(phoneNumber);
        return ResponseEntity.ok().body(RestAPIResponse.success(code));
    }

    @PostMapping("/api/v1/authentication-code/email/check")
    public ResponseEntity<?> checkAuthenticationCodeEmail(@Valid @RequestBody String code) throws Exception {
        if (Objects.equals(emailService.getAuthCode(), code)) {
            return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success("invalid"));
    }

    @PostMapping("/api/v1/authentication-code/sms/check")
    public ResponseEntity<?> checkAuthenticationCodeSMS(@Valid @RequestBody String code) throws Exception {
        if (Objects.equals(smsService.getAuthCode(), code)) {
            return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
        }
        return ResponseEntity.ok().body(RestAPIResponse.success("invalid"));
    }

    @PostMapping("/api/v1/email/check")
    public ResponseEntity<?> checkEmail(@Valid @RequestBody String email) throws Exception {
        User user = userService.findByUsername(email);
        if (user == null) {
            return ResponseEntity.ok().body(RestAPIResponse.success("invalid"));
        }

        if (socialLoginRepository.existsByUser(user)) {
            return ResponseEntity.ok().body(RestAPIResponse.success("valid-incorrect"));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success("valid-correct"));
    }

    @PostMapping("/api/v1/password/check")
    public ResponseEntity<?> checkPassword(@Valid @RequestBody SignInRequest request) throws Exception {
        User user = userService.findByUsername(request.getUsername());
        String encodedPassword = user.getPassword().getPw();

        if (passwordUtils.matches(request.getPassword(), encodedPassword)) {
            return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success("invalid"));
    }

    // 상품 검색 (예: 같은 조건으로 page 만 다르게 하면 다음 페이지 목록 반환)
    @GetMapping("/api/v1/goods")
    public ResponseEntity<?> getGoodsList(@RequestParam(value = "name", required = false) String name,
                                          @RequestParam(value = "type", required = false) String type,
                                          @RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "size", required = false) Integer size,
                                          @RequestParam(value = "sortBy", required = false) String sortBy) throws Exception {
        List<IGoodsView> goodsList = goodsService.findGoodsList(name, type, page, size, sortBy);
        if (goodsList.isEmpty()) {
            return ResponseEntity.ok().body(RestAPIResponse.success(new ArrayList<>()));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success(goodsList));
    }

    // 상품 전체 개수
    @GetMapping("/api/v1/goods/count")
    public ResponseEntity<?> getGoodsListCount(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "type", required = false) String type) throws Exception {
        return ResponseEntity.ok().body(RestAPIResponse.success(goodsService.getGoodsListCount(name, type)));
    }

    // 주문하기
    @APILoginState
    @PostMapping("/api/v1/order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrdersRequest request) throws Exception {
        User user = userService.findByUsername(request.getUsername());
        List<Goods> goodsList = goodsService.findSimpleGoodsListByGoodsNames(request.getGoodsNameList());
        if (goodsList.size() != request.getGoodsNameList().size()) {
            return ResponseEntity.ok().body(RestAPIResponse.success("exists invalid goods"));
        }

        boolean completedOrders = ordersService.createOrders(user, goodsList, request.getGoodsCountList());
        if (!completedOrders) {
            ResponseEntity.ok().body(RestAPIResponse.success("invalid orders"));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
    }

    // 베스트셀러 가져오기
    @GetMapping("/api/v1/goods/best-seller/{days}")
    public ResponseEntity<?> getBestSellerGoods(@Valid @PathVariable("days") Short days) throws Exception {
        Map<String, Object> bestSellerWithSalesCount = ordersService.getBestSellerGoodsByDate(days);
        return ResponseEntity.ok().body(RestAPIResponse.success(bestSellerWithSalesCount));
    }

    // 인기상품 리스트 불러오기
    @GetMapping("/api/v1/goods/popular/{days}")
    public ResponseEntity<?> getPopularGoodsList(@Valid @PathVariable("days") Short days) throws Exception {
        List<IGoodsView> popularGoodsList = userActivityLogService.findBestViewsUserActivityList(0, 10, days);
        return ResponseEntity.ok().body(RestAPIResponse.success(popularGoodsList));
    }

    // 랜덤으로 아무 상품이나 불러오기
    @GetMapping("/api/v1/goods/random")
    public ResponseEntity<?> getRandomGoods() throws Exception {
        return ResponseEntity.ok().body(RestAPIResponse.success(goodsService.findGoodsDetailsRandom().get()));
    }

    // 장바구니에 상품 저장
    @APILoginState
    @PostMapping("/api/v1/cart")
    public ResponseEntity<?> createCart(@Valid @RequestBody CartRequest request) throws Exception {
        User user = userService.findByUsername(request.getUsername());
        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body(RestAPIResponse.success("invalid goods"));
        }

        cartService.addCart(user, goods, request.getGoodsCount());

        return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
    }

    // 장바구니 불러오기
    @APILoginState
    @GetMapping("/api/v1/cart")
    public ResponseEntity<?> getCart(@Valid @RequestParam String username) throws Exception {
        User user = userService.findByUsername(username);
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
            CartResponse cartResponse = CartResponse.builder().goodsView((GoodsView) gv).goodsCount(goodsNameWithCount.get(gv.getName())).build();

            cartResponseList.add(cartResponse);
        }

        if (cartResponseList.isEmpty()) {
            return ResponseEntity.ok().body(RestAPIResponse.success(new ArrayList<>()));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success(cartResponseList));
    }

    // 장바구니에서 상품 제거
    @APILoginState
    @DeleteMapping("/api/v1/cart")
    public ResponseEntity<?> removeCart(@Valid @RequestBody CartRequest request) throws Exception {
        User user = userService.findByUsername(request.getUsername());
        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body(RestAPIResponse.success("invalid goods"));
        }

        cartService.removeCart(user, goods);

        return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
    }

    // 위시리스트에 상품 저장
    @APILoginState
    @PostMapping("/api/v1/wishlist")
    public ResponseEntity<?> createWishList(@Valid @RequestBody FavoriteRequest request) throws Exception {
        User user = userService.findByUsername(request.getUsername());
        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body(RestAPIResponse.success("invalid goods"));
        }

        favoriteService.addWishList(user, goods);

        return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
    }

    // 위시리스트 불러오기
    @APILoginState
    @GetMapping("/api/v1/wishlist")
    public ResponseEntity<?> getWishList(@Valid @RequestParam String username) throws Exception {
        User user = userService.findByUsername(username);
        List<FavoriteGoods> favoriteGoodsList = favoriteService.getWishList(user);
        List<String> goodsNameList = new ArrayList<>();

        for (FavoriteGoods fg : favoriteGoodsList) {
            goodsNameList.add(fg.getGoods().getName());
        }

        List<IGoodsView> goodsViewList = goodsService.findGoodsListByGoodsName(goodsNameList);
        List<FavoriteResponse> favoriteResponseList = new ArrayList<>();

        for (IGoodsView gv : goodsViewList) {
            FavoriteResponse favoriteResponse = FavoriteResponse.builder().goodsView((GoodsView) gv).build();

            favoriteResponseList.add(favoriteResponse);
        }

        if (favoriteResponseList.isEmpty()) {
            return ResponseEntity.ok().body(RestAPIResponse.success(new ArrayList<>()));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success(favoriteResponseList));
    }

    // 위시리스트에서 상품 제거
    @APILoginState
    @DeleteMapping("/api/v1/wishlist")
    public ResponseEntity<?> removeWishList(@Valid @RequestBody FavoriteRequest request) throws Exception {
        User user = userService.findByUsername(request.getUsername());
        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body(RestAPIResponse.success("invalid goods"));
        }

        favoriteService.removeWishList(user, goods);
        return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
    }

    // 이미 위시리스트에 있는 상품인지 확인
    @APILoginState
    @PostMapping("/api/v1/wishlist/checked")
    public ResponseEntity<?> checkWishListGoods(@Valid @RequestBody FavoriteRequest request) throws Exception {
        User user = userService.findByUsername(request.getUsername());
        Goods goods = goodsService.findSimpleGoodsByGoodsName(request.getGoodsName());
        if (goods == null) {
            return ResponseEntity.ok().body(RestAPIResponse.success(false));
        }

        List<FavoriteGoods> favoriteGoodsList = favoriteService.getWishList(user);

        for (FavoriteGoods fg : favoriteGoodsList) {
            if (fg.getGoods().getName().equals(request.getGoodsName())) {
                return ResponseEntity.ok().body(RestAPIResponse.success(true));
            }
        }

        return ResponseEntity.ok().body(RestAPIResponse.success(false));
    }

    // device id 생성
    @GetMapping("/api/v1/device/id")
    public ResponseEntity<?> getDeviceId() throws Exception {
        return ResponseEntity.ok().body(RestAPIResponse.success(UUID.randomUUID()));
    }

    // 사용자 행동 로그 불러오기 (상품)
    @GetMapping("/api/v1/recommendation/log/activity/goods")
    @ResponseBody
    public ResponseEntity<?> getRecommendationActivityLogGoods(@Valid @RequestParam UUID deviceId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.ok().body(RestAPIResponse.success("invalid user"));
        }

        List<UserActivityLogElements> userActivityLogElements = userActivityLogService.findUserActivityListByDeviceIdAndTargetCode(deviceId, UserActivityLog.ETargetCode.GOODS);
        List<String> targetNameList = new ArrayList<>();
        for (UserActivityLogElements ual : userActivityLogElements) {
            targetNameList.add(ual.getTargetName());
        }

        return ResponseEntity.ok().body(RestAPIResponse.success(targetNameList));
    }

    // 사용자 행동 로그 제거 (상품)
    @DeleteMapping("/api/v1/recommendation/log/activity/goods")
    @ResponseBody
    public ResponseEntity<?> removeRecommendationActivityLogGoods(@Valid @RequestBody RemoveUserActivityLogGoodsRequest request) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.ok().body(RestAPIResponse.success("invalid user"));
        }

        userActivityLogService.removeUserActivityLog(request.getDeviceId(), request.getTargetName(), UserActivityLog.ETargetCode.GOODS);
        return ResponseEntity.ok().body(RestAPIResponse.success(null));
    }

    @PostMapping("/api/v1/recommendation/base")
    public ResponseEntity<?> setRecommendationBase(@Valid @RequestBody UserRecommendationBaseRequest userRecommendationBaseRequest) {
        String username = userRecommendationBaseRequest.getUsername();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().body(RestAPIResponse.success("redirect"));
        }

        if (recommendationUtils.setUserRecommendationBase(userRecommendationBaseRequest)) {
            return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success("invalid"));
    }

    @PostMapping("/api/v1/recommendation/daily")
    public ResponseEntity<?> setRecommendationDaily(@Valid @RequestBody UserRecommendationDailyRequest userRecommendationDailyRequest) {
        String username = userRecommendationDailyRequest.getUsername();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().body(RestAPIResponse.success("redirect"));
        }

        if (recommendationUtils.setUserRecommendationDaily(userRecommendationDailyRequest)) {
            return ResponseEntity.ok().body(RestAPIResponse.success("valid"));
        }

        return ResponseEntity.ok().body(RestAPIResponse.success("invalid"));
    }

    @GetMapping("/api/v1/recommendation/daily")
    public ResponseEntity<?> getRecommendationDaily(@AuthenticationPrincipal String username) {
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().body(RestAPIResponse.success("redirect"));
        }

        List<IGoodsView> goodsViews = recommendationUtils.getUserRecommendationGoodsList(username);
        return ResponseEntity.ok().body(RestAPIResponse.success(goodsViews));
    }

    @GetMapping("/api/v1/recommendation/base/keyword")
    public ResponseEntity<?> getRecommendationBaseKeyword(@AuthenticationPrincipal String username) {
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().body(RestAPIResponse.success("redirect"));
        }

        String categories = recommendationUtils.getPreferredCategories(username);
        return ResponseEntity.ok().body(RestAPIResponse.success(categories));
    }

    @GetMapping("/api/v1/recommendation/daily/feeling")
    public ResponseEntity<?> getRecommendationDailyFeeling(@AuthenticationPrincipal String username) {
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().body(RestAPIResponse.success("redirect"));
        }

        String feeling = recommendationUtils.getDailyFeeling(username);
        return ResponseEntity.ok().body(RestAPIResponse.success(feeling));
    }
}
