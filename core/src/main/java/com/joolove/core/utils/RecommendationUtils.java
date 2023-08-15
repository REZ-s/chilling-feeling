package com.joolove.core.utils;

import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.product.Category;
import com.joolove.core.domain.recommendation.UserRecommendationBase;
import com.joolove.core.domain.recommendation.UserRecommendationDaily;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityLogElements;
import com.joolove.core.dto.query.UserRecommendationElements;
import com.joolove.core.dto.request.UserRecommendationBaseRequest;
import com.joolove.core.dto.request.UserRecommendationDailyRequest;
import com.joolove.core.service.GoodsService;
import com.joolove.core.service.UserActivityLogService;
import com.joolove.core.service.UserRecommendationService;
import com.joolove.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RecommendationUtils {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationUtils.class);
    private final UserService userService;
    private final GoodsService goodsService;
    private final UserRecommendationService userRecommendationService;
    private final UserActivityLogService userActivityLogService;

    // 사용자별 추천 상품 리스트를 가져온다.
    public List<IGoodsView> getUserRecommendationGoodsList(String username) {
        // 사용자의 행동 패턴 데이터가 변한 것이 없다면, 기존에 저장된 추천 요소를 가져온다. (서비스 진입 시, 호출 후 저장됨)
        // '사용자 상품 추천 리스트' = '기본 추천' 과 '오늘의 추천' 을 기반으로 '최근 사용자 행동 데이터 요소 5개' 를 분석하여 추천
        // 리스트 인덱스 (0 : abvLimit, 1 : preferredCategory, 2 : recentFeeling, 3~ : userActivityElements)
        List<IGoodsView> userRecommendationGoodsList;

        UserRecommendationElements userRecommendationElements = getUserRecommendElements(username);   // 개인 추천 관련 데이터 가져오기
        List<UserActivityLogElements> userActivityLogElementsList = getUserActivityElements(username);    // 사용자 행동 데이터 가져오기

        String abvLimit = userRecommendationElements.getAbvLimit();
        List<Category.ECategory> preferredCategories = userRecommendationElements.getPreferredCategories();
        List<String> typeOrLabelList = new ArrayList<>();
        for (Category.ECategory category : preferredCategories) {
            String nowTypeOrLabel = getKRTypeOrLabel(category);
            if (Objects.equals(nowTypeOrLabel, "전체")) {
                continue;
            }
            typeOrLabelList.add(nowTypeOrLabel);
        }

        UserRecommendationDaily.EEmotion recentFeeling = userRecommendationElements.getRecentFeeling();
        UserRecommendationBase.EFigure feeling = getSweetnessByFeeling(recentFeeling);
        List<String> goodsNameList = new ArrayList<>();
        for (UserActivityLogElements u : userActivityLogElementsList) {
            goodsNameList.add(u.getTargetName());
        }

        userRecommendationGoodsList = goodsService.getRecommendationGoodsList(
                    abvLimit, typeOrLabelList, feeling, goodsNameList);

        return userRecommendationGoodsList;
    }

    private String getKRTypeOrLabel(Category.ECategory category) {
        return switch (category) {
            case WINE -> "와인";           // GoodsDetails type
            case NON_ALCOHOL -> "논알콜";  // GoodsDetails type
            case WHISKY -> "위스키";       // GoodsDetails type
            case COCKTAIL -> "칵테일";     // GoodsDetails type
            case TRADITIONAL_LIQUOR -> "전통주";    // GoodsDetails type
            case MEAT -> "레드와인";                 // GoodsDetails type
            case SEAFOOD -> "화이트와인";            // GoodsDetails type
            case NEW -> "NEW";      // GoodsStats label
            case BEST -> "BEST";    // GoodsStats label
            case HOT -> "HOT";      // GoodsStats label
            default -> "전체";    // GoodsDetails type
        };
    }

    private UserRecommendationBase.EFigure getSweetnessByFeeling(UserRecommendationDaily.EEmotion recentFeeling) {
        UserRecommendationBase.EFigure mySweetness = UserRecommendationBase.EFigure.UNKNOWN;

        if (recentFeeling.equals(UserRecommendationDaily.EEmotion.HAPPY) || recentFeeling.equals(UserRecommendationDaily.EEmotion.SMILE)) {
            mySweetness = UserRecommendationBase.EFigure.HIGH;
        } else if (recentFeeling.equals(UserRecommendationDaily.EEmotion.SAD) || recentFeeling.equals(UserRecommendationDaily.EEmotion.ANGRY)) {
            mySweetness = UserRecommendationBase.EFigure.LOW;
        }

        return mySweetness;
    }

    // 최신 상품 리스트를 가져온다.
    private List<IGoodsView> getNewGoodsList() {
        return goodsService.findNewGoodsListDefault();
    }

    // 인기 상품 리스트를 가져온다.
    private List<IGoodsView> getPopularGoodsList() {
        return userActivityLogService.findBestViewsUserActivityListDefault();
    }

    // 사용자가 '기본 추천' 을 설정한다.
    public boolean setUserRecommendationBase(UserRecommendationBaseRequest userRecommendationBaseRequest) {
        try {
            updateUserRecommendationBase(userRecommendationBaseRequest);
        } catch (Exception e) {
            logger.error("setUserRecommendationBase error ", e);
            return false;
        }

        return true;
    }

    // 사용자가 '오늘의 추천' 을 설정한다.
    public boolean setUserRecommendationDaily(UserRecommendationDailyRequest userRecommendationDailyRequest) {
        try {
            updateUserRecommendationDaily(userRecommendationDailyRequest);
        } catch (Exception e) {
            logger.error("setUserRecommendationDaily error ", e);
            return false;
        }

        return true;
    }

    // 사용자가 어떤 상품을 직접 '클릭' 하거나 '검색' 할 때 호출된다.
    public boolean setUserActivityRecommendation(UserActivityLogElements userActivityLogElements) {
        try {
            updateUserActivityElements(userActivityLogElements);
        } catch (Exception e) {
            logger.error("setUserActivityRecommendation error ", e);
            return false;
        }

        return true;
    }

    private void updateUserRecommendationBase(UserRecommendationBaseRequest userRecommendationBaseRequest)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(userRecommendationBaseRequest.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + userRecommendationBaseRequest.getUsername());
        }

        UserRecommendationBase userRecommendationBase = UserRecommendationBase.builder()
                    .user(user)
                    .abvLimit(userRecommendationBaseRequest.getAbvLimit())
                    .preferredCategories(String.join(",", userRecommendationBaseRequest.getPreferredCategory()))
                    .build();

        if (userRecommendationService.findUserRecommendationBase(user) == null) {
            userRecommendationService.addUserRecommendationBase(userRecommendationBase);
        } else {
            userRecommendationService.updateUserRecommendationBase(userRecommendationBase);
        }
    }

    private void updateUserRecommendationDaily(UserRecommendationDailyRequest userRecommendationDailyRequest)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(userRecommendationDailyRequest.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + userRecommendationDailyRequest.getUsername());
        }

        String feeling = userRecommendationDailyRequest.getRecentFeeling();
        UserRecommendationDaily.EEmotion recentFeeling = UserRecommendationDaily.EEmotion.BLANK;

        if (Objects.equals(feeling, "기뻐요")) {
            recentFeeling = UserRecommendationDaily.EEmotion.HAPPY;
        } else if (Objects.equals(feeling, "즐거워요")) {
            recentFeeling = UserRecommendationDaily.EEmotion.SMILE;
        } else if (Objects.equals(feeling, "슬퍼요")) {
            recentFeeling = UserRecommendationDaily.EEmotion.SAD;
        } else if (Objects.equals(feeling, "화나요")) {
            recentFeeling = UserRecommendationDaily.EEmotion.ANGRY;
        }

        UserRecommendationDaily userRecommendationDaily = UserRecommendationDaily.builder()
                .user(user)
                .feeling(recentFeeling)
                .build();

        if (userRecommendationService.findUserRecommendationDaily(user) == null) {
            userRecommendationService.addUserRecommendationDaily(userRecommendationDaily);
        } else {
            userRecommendationService.updateUserRecommendationDaily(userRecommendationDaily);
        }
    }

    private void updateUserActivityElements(UserActivityLogElements userActivityLogElements)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(userActivityLogElements.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + userActivityLogElements.getUsername());
        }

        UserActivityLog userActivityLog = UserActivityLog.builder()
                .username(userActivityLogElements.getUsername())
                .targetCode(userActivityLogElements.getTargetCode())
                .targetName(userActivityLogElements.getTargetName())
                .activityCode(userActivityLogElements.getActivityCode())
                .activityDescription(userActivityLogElements.getActivityDescription())
                .build();

        userActivityLogService.addUserActivityLog(userActivityLog);
    }

    // 최근에 수집된 사용자 행동 데이터 (DTO : UserActivityElements) 를 가져온다.
    private List<UserActivityLogElements> getUserActivityElements(String username)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

        // 최근 5개를 target 이 goods 인 경우에만 가져온다.
        return userActivityLogService.findUserActivityListByUsername(username, UserActivityLog.ETargetCode.GOODS);
    }

    // 기존에 저장해두었던 사용자 추천 관련 데이터 (DTO : UserRecommendElements) 를 가져온다.
    private UserRecommendationElements getUserRecommendElements(String username)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

        UserRecommendationBase userRecommendationBase = userRecommendationService.findUserRecommendationBase(user);
        String abvLimit = "100";
        String preferredCategories = Category.ECategory.ALL.name();
        if (userRecommendationBase != null) {
            abvLimit = userRecommendationBase.getAbvLimit();
            preferredCategories = userRecommendationBase.getPreferredCategories();
        }

        UserRecommendationDaily userRecommendationDaily = userRecommendationService.findUserRecommendationDaily(user);
        UserRecommendationDaily.EEmotion dailyFeeling = UserRecommendationDaily.EEmotion.BLANK;
        if (userRecommendationDaily != null) {
            dailyFeeling = userRecommendationDaily.getFeeling();
        }

        String[] pcStrings = preferredCategories.split(",");
        List<Category.ECategory> ePreferredCategories = new ArrayList<>();
        for (String pcString : pcStrings) {
            ePreferredCategories.add(getECategory(pcString));
        }

        return UserRecommendationElements.builder()
                .username(username)
                .abvLimit(abvLimit)
                .preferredCategories(ePreferredCategories)
                .recentFeeling(dailyFeeling)
                .build();
    }

    private Category.ECategory getECategory(String pcString) {
        Category.ECategory ePreferredCategory = Category.ECategory.ALL;

        if (pcString.equals(Category.ECategory.WINE.getDisplayName())) {
            ePreferredCategory = Category.ECategory.WINE;
        } else if (pcString.equals(Category.ECategory.WHISKY.getDisplayName())) {
            ePreferredCategory = Category.ECategory.WHISKY;
        } else if (pcString.equals(Category.ECategory.TRADITIONAL_LIQUOR.getDisplayName())) {
            ePreferredCategory = Category.ECategory.TRADITIONAL_LIQUOR;
        } else if (pcString.equals(Category.ECategory.COCKTAIL.getDisplayName())) {
            ePreferredCategory = Category.ECategory.COCKTAIL;
        } else if (pcString.equals(Category.ECategory.NON_ALCOHOL.getDisplayName())) {
            ePreferredCategory = Category.ECategory.NON_ALCOHOL;
        } else if (pcString.equals(Category.ECategory.MEAT.getDisplayName())) {
            ePreferredCategory = Category.ECategory.MEAT;
        } else if (pcString.equals(Category.ECategory.SEAFOOD.getDisplayName())) {
            ePreferredCategory = Category.ECategory.SEAFOOD;
        } else if (pcString.equals(Category.ECategory.NEW.getDisplayName())) {
            ePreferredCategory = Category.ECategory.NEW;
        } else if (pcString.equals(Category.ECategory.BEST.getDisplayName())) {
            ePreferredCategory = Category.ECategory.BEST;
        } else if (pcString.equals(Category.ECategory.HOT.getDisplayName())) {
            ePreferredCategory = Category.ECategory.HOT;
        }
        return ePreferredCategory;
    }

    // 모든 추천 요소들을 하나의 리스트에 담는다.
    private List<Object> getAllRecommendationElements(String username) {
        List<Object> userRecommendationElementsList = new ArrayList<>();
        UserRecommendationElements userRecommendationElements = getUserRecommendElements(username);   // 개인 추천 관련 데이터 가져오기
        List<UserActivityLogElements> userActivityLogElementsList = getUserActivityElements(username);    // 사용자 행동 데이터 가져오기

        // 개인별 추천 요소
        userRecommendationElementsList.add(userRecommendationElements.getAbvLimit());        // 기본 추천
        userRecommendationElementsList.add(userRecommendationElements.getPreferredCategories());  // 기본 추천
        userRecommendationElementsList.add(userRecommendationElements.getRecentFeeling());       // 오늘의 추천

        // 사용자 행동 데이터 기반 추천 요소
        for (UserActivityLogElements e : userActivityLogElementsList) {
            userRecommendationElementsList.add(e.getTargetName());
        }

        return userRecommendationElementsList;
    }

    public String getPreferredCategories(String username) {
        User user = userService.findByUsername(username);
        UserRecommendationBase recommendationBase = userRecommendationService.findUserRecommendationBase(user);
        return recommendationBase.getPreferredCategories();
    }

    public String getDailyFeeling(String username) {
        User user = userService.findByUsername(username);
        UserRecommendationDaily recommendationDaily = userRecommendationService.findUserRecommendationDaily(user);
        return recommendationDaily.getFeeling().name();
    }

}
