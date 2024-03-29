package com.joolove.core.controller;

import com.joolove.core.domain.product.Category;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.GoodsDetails;
import com.joolove.core.domain.product.GoodsStats;
import com.joolove.core.dto.request.AdminCreateGoodsRequest;
import com.joolove.core.repository.GoodsDetailsRepository;
import com.joolove.core.repository.GoodsStatsRepository;
import com.joolove.core.service.GoodsService;
import com.joolove.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
public class AdminController {
    private final UserService userService;
    private final GoodsService goodsService;
    private final GoodsDetailsRepository goodsDetailsRepository;
    private final GoodsStatsRepository goodsStatsRepository;

    // 상품 추가 페이지
    @GetMapping("/admin/goods")
    public String createGoodsPage() {
        return "cf_admin_goods_page";
    }

    // 상품 추가
    @PostMapping("/api/v1/admin/goods")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> createGoods(@ModelAttribute("request") AdminCreateGoodsRequest request) {
        String type = request.getType();
        String categoryName = switch (type) {
            case "와인" -> Category.ECategory.WINE.name();
            case "위스키" -> Category.ECategory.WHISKY.name();
            case "칵테일" -> Category.ECategory.COCKTAIL.name();
            case "전통주" -> Category.ECategory.TRADITIONAL_LIQUOR.name();
            case "논알콜" -> Category.ECategory.NON_ALCOHOL.name();
            default -> Category.ECategory.ALL.name();
        };

        Goods goods = Goods.builder()
                .name(request.getName())
                .categoryName(categoryName)
                .salesStatus(request.getSalesStatus())
                .salesFigures(0L)
                .price(99999)
                .stock(99999)
                .build();

        GoodsDetails goodsDetails = GoodsDetails.alcoholBuilder()
                .goods(goods)
                .name(request.getName())
                .engName(request.getEngName())
                .type(type)
                .subType(request.getSubType())
                .imageUrl(request.getImageUrl())
                .color(request.getColor())
                .colorImageUrl(request.getColorImageUrl())
                .descriptionImageUrl(request.getDescriptionImageUrl())
                .description(request.getDescription())
                .summary(request.getSummary())
                .country(request.getCountry())
                .company(request.getCompany())
                .supplier(request.getSupplier())
                .degree(request.getDegree())
                .opt1Value(request.getAroma())
                .opt2Value(request.getBalance())
                .opt3Value(request.getBody())
                .opt4Value(request.getTannin())
                .opt5Value(request.getAcidity())
                .opt6Value(request.getSweetness())
                .opt7Value(request.getSoda())
                .build();

        GoodsStats goodsStats = GoodsStats.builder()
                .goods(goods)
                .label(request.getLabel())
                .score(request.getScore())
                .reviewCount(request.getReviewCount())
                .build();

        goodsService.addGoods(goods);
        goodsDetailsRepository.save(goodsDetails);
        goodsStatsRepository.save(goodsStats);

        return ResponseEntity.ok().body("Success");
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
