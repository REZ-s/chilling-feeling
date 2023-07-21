package com.joolove.core.controller;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.GoodsDetails;
import com.joolove.core.domain.product.GoodsStats;
import com.joolove.core.repository.GoodsDetailsRepository;
import com.joolove.core.repository.GoodsStatsRepository;
import com.joolove.core.service.GoodsService;
import com.joolove.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
public class AdminController {
    private final UserService userService;
    private final GoodsService goodsService;
    private final GoodsDetailsRepository goodsDetailsRepository;
    private final GoodsStatsRepository goodsStatsRepository;

    // 상품 추가
    @PostMapping("/admin/goods")
    @ResponseBody
    public ResponseEntity<?> addGoods() {
        Goods goods = Goods.builder()
                .name("짐빔 화이트")
                .salesStatus((short)1)
                .build();

        GoodsDetails goodsDetails = GoodsDetails.alcoholBuilder()
                .goods(goods)
                .name("짐빔 화이트")
                .engName("Jim Beam White")
                .color("white")
                .colorImageUrl("https://cdn.veluga.kr/icons/tasting-note/color/brick-red.svg")
                .description("테스트 위스키")
                .descriptionImageUrl("/images/item-rep01.png")
                .summary("테스트 위스키")
                .country("korea")
                .company("테스트 컴퍼니")
                .supplier("테스트 컴퍼니")
                .degree("10.0")
                .imageUrl("/images/item-rep01.png")
                .type("위스키")
                .opt1Value("opt1Value")
                .opt2Value("opt2Value")
                .opt3Value("opt3Value")
                .opt4Value("opt4Value")
                .opt5Value("opt5Value")
                .opt6Value("opt6Value")
                .opt7Value("opt7Value")
                .build();

        GoodsStats goodsStats = GoodsStats.builder()
                .goods(goods)
                .label("가성비")
                .score("4.4")
                .reviewCount(100)
                .build();

        goodsService.addGoods(goods);
        goodsDetailsRepository.save(goodsDetails);
        goodsStatsRepository.save(goodsStats);

        return ResponseEntity.ok().body("valid");
    }

    // 모든 사용자 계정 가져오기
    @GetMapping("/admin/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "cf_admin_users_page";
    }

    // 접근 권한 확인
    @GetMapping("/admin/all/access")
    @ResponseBody
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/admin/user/access")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_User')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin/manager/access")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String managerAccess() {
        return "Manager Board.";
    }

    @GetMapping("/admin/admin/access")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
