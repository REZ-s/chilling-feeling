package com.joolove.core.utils.algorithm;

import com.joolove.core.domain.ECategory;
import com.joolove.core.domain.EEmotion;
import com.joolove.core.domain.EFigure;
import com.joolove.core.domain.ETargetCode;
import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.recommendation.UserRecommendationBase;
import com.joolove.core.domain.recommendation.UserRecommendationDaily;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityElements;
import com.joolove.core.dto.request.UserRecommendationElements;
import com.joolove.core.service.GoodsService;
import com.joolove.core.service.UserActivityService;
import com.joolove.core.service.UserRecommendationService;
import com.joolove.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.OracleJoinFragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RecommendationComponent {
    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserRecommendationService userRecommendationService;
    @Autowired
    private UserActivityService userActivityService;

    // 사용자별 추천 상품 리스트를 가져온다.
    public List<IGoodsView> getUserRecommendationGoodsList(String username) {
        // 사용자의 행동 패턴 데이터가 변한 것이 없다면, 기존에 저장된 추천 요소를 가져온다. (서비스 진입 시, 호출 후 저장됨)
        // '사용자 상품 추천 리스트' = '기본 추천' 과 '오늘의 추천' 을 기반으로 '최근 사용자 행동 데이터 요소 5개' 를 분석하여 추천
        // 리스트 인덱스 (0 : abvLimit, 1 : preferredCategory, 2 : recentFeeling, 3~ : userActivityElements)
        List<IGoodsView> userRecommendationGoodsList;

        UserRecommendationElements userRecommendationElements = getUserRecommendElements(username);   // 개인 추천 관련 데이터 가져오기
        List<UserActivityElements> userActivityElementsList = getUserActivityElements(username);    // 사용자 행동 데이터 가져오기

        Short abvLimit = userRecommendationElements.getAbvLimit();
        ECategory preferredCategory = userRecommendationElements.getPreferredCategory();
        EEmotion recentFeeling = userRecommendationElements.getRecentFeeling();
        List<String> goodsNameList = new ArrayList<>();
        for (UserActivityElements u : userActivityElementsList) {
            goodsNameList.add(u.getTargetName());
        }

        userRecommendationGoodsList = goodsService.getRecommendationGoodsList(
                    abvLimit, getKRTypeOrLabel(preferredCategory), getSweetnessByFeeling(recentFeeling), goodsNameList);

        return userRecommendationGoodsList;
    }

    private String getKRTypeOrLabel(ECategory category) {
        return switch (category) {
            case WINE -> "와인";           // GoodsDetails type
            case NON_ALCOHOL -> "논알콜";  // GoodsDetails type
            case WHISKY -> "위스키";       // GoodsDetails type
            case COCKTAIL -> "칵테일";     // GoodsDetails type
            case TRADITIONAL_LIQUOR -> "전통주";   // GoodsDetails type
            case MEAT -> "레드와인";                // GoodsDetails type
            case SEAFOOD -> "화이트와인";            // GoodsDetails type
            case BRAND_NEW -> "신상품";    // GoodsStats label
            case BEST_SELLER -> "베스트";  // GoodsStats label
            default -> "전체";    // GoodsDetails type
        };
    }

    private EFigure getSweetnessByFeeling(EEmotion recentFeeling) {
        EFigure mySweetness = EFigure.UNKNOWN;

        if (recentFeeling.equals(EEmotion.HAPPY) || recentFeeling.equals(EEmotion.SMILE)) {
            mySweetness = EFigure.HIGH;
        } else if (recentFeeling.equals(EEmotion.SAD) || recentFeeling.equals(EEmotion.ANGRY)) {
            mySweetness = EFigure.LOW;
        }

        return mySweetness;
    }

    // 최신 상품 리스트를 가져온다.
    private List<IGoodsView> getNewGoodsList() {
        return goodsService.findNewGoodsListDefault();
    }

    // 인기 상품 리스트를 가져온다.
    private List<IGoodsView> getPopularGoodsList() {
        return userActivityService.findBestViewsUserActivityListDefault();
    }

    // 사용자의 '기본 추천' 과 '오늘의 추천' 을 설정한다.
    public boolean setUserRecommendation(String username, UserRecommendationElements userRecommendationElements) {
        try {
            updateUserRecommendElements(username, userRecommendationElements);
        } catch (Exception e) {
            log.error("setUserRecommendation error : {}", e.getMessage());
            return false;
        }

        return true;
    }

    // 사용자가 어떤 상품을 직접 '클릭' 하거나 '검색' 할 때 호출된다.
    public boolean setUserActivityRecommendation(String username, UserActivityElements userActivityElements) {
        try {
            updateUserActivityElements(username, userActivityElements);
        } catch (Exception e) {
            log.error("setUserActivityRecommendation error : {}", e.getMessage());
            return false;
        }

        return true;
    }

    private void updateUserRecommendElements(String username, UserRecommendationElements userRecommendationElements)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

        UserRecommendationBase userRecommendationBase = UserRecommendationBase.builder()
                    .user(user)
                    .abvLimit(userRecommendationElements.getAbvLimit())
                    .preferredCategory(userRecommendationElements.getPreferredCategory())
                    .build();

        UserRecommendationDaily userRecommendationDaily = UserRecommendationDaily.builder()
                    .user(user)
                    .feeling(userRecommendationElements.getRecentFeeling())
                    .build();

        userRecommendationService.addUserRecommendation(userRecommendationBase, userRecommendationDaily);
    }

    private void updateUserActivityElements(String username, UserActivityElements userActivityElements)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

        UserActivityLog userActivityLog = UserActivityLog.builder()
                .user(user)
                .targetCode(userActivityElements.getTargetCode())
                .targetName(userActivityElements.getTargetName())
                .activityCode(userActivityElements.getActivityCode())
                .activityDescription(userActivityElements.getActivityDescription())
                .build();

        userActivityService.addUserActivity(userActivityLog);
    }

    // 최근에 수집된 사용자 행동 데이터 (DTO : UserActivityElements) 를 가져온다.
    private List<UserActivityElements> getUserActivityElements(String username)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

        // 최근 5개를 target 이 goods 인 경우에만 가져온다.
        return userActivityService.findUserActivityListByUsername(username, ETargetCode.GOODS);
    }

    // 기존에 저장해두었던 사용자 추천 관련 데이터 (DTO : UserRecommendElements) 를 가져온다.
    private UserRecommendationElements getUserRecommendElements(String username)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

        UserRecommendationBase userRecommendationBase = userRecommendationService.findUserRecommendationBase(user);
        Short abvLimit = 100;
        ECategory preferredCategory = ECategory.ALL;
        if (userRecommendationBase != null) {
            abvLimit = userRecommendationBase.getAbvLimit();
            preferredCategory = userRecommendationBase.getPreferredCategory();
        }

        UserRecommendationDaily userRecommendationDaily = userRecommendationService.findUserRecommendationDaily(user);
        EEmotion dailyFeeling = EEmotion.BLANK;
        if (userRecommendationDaily != null) {
            dailyFeeling = userRecommendationDaily.getFeeling();
        }

        return UserRecommendationElements.builder()
                .username(username)
                .abvLimit(abvLimit)
                .preferredCategory(preferredCategory)
                .recentFeeling(dailyFeeling)
                .build();
    }

    // 모든 추천 요소들을 하나의 리스트에 담는다.
    private List<Object> getAllRecommendationElements(String username) {
        List<Object> userRecommendationElementsList = new ArrayList<>();
        UserRecommendationElements userRecommendationElements = getUserRecommendElements(username);   // 개인 추천 관련 데이터 가져오기
        List<UserActivityElements> userActivityElementsList = getUserActivityElements(username);    // 사용자 행동 데이터 가져오기

        // 개인별 추천 요소
        userRecommendationElementsList.add(getDegreeLimit(userRecommendationElements));        // 기본 추천
        userRecommendationElementsList.add(getPreferredCategory(userRecommendationElements));  // 기본 추천
        userRecommendationElementsList.add(getDailyFeeling(userRecommendationElements));       // 오늘의 추천

        // 사용자 행동 데이터 기반 추천 요소
        for (UserActivityElements e : userActivityElementsList) {
            userRecommendationElementsList.add(e.getTargetName());
        }

        return userRecommendationElementsList;
    }

    private EEmotion getDailyFeeling(UserRecommendationElements userRecommendationElements) {
        return userRecommendationElements.getRecentFeeling();
    }

    private ECategory getPreferredCategory(UserRecommendationElements userRecommendationElements) {
        return userRecommendationElements.getPreferredCategory();
    }

    private String getDegreeLimit(UserRecommendationElements userRecommendationElements) {
        return userRecommendationElements.getAbvLimit().toString();
    }

    private boolean updateDailyFeeling(String username, String feeling) {
        UserRecommendationElements userRecommendationElements;

        try {
            userRecommendationElements = getUserRecommendElements(username);
        } catch (Exception e) {
            log.error("updateDailyFeeling error : {}", e.getMessage());
            return false;
        }

        if (feeling.equals(EEmotion.SMILE.name())) {
            userRecommendationElements.setRecentFeeling(EEmotion.SMILE);
        } else if (feeling.equals(EEmotion.HAPPY.name())) {
            userRecommendationElements.setRecentFeeling(EEmotion.HAPPY);
        } else if (feeling.equals(EEmotion.SAD.name())) {
            userRecommendationElements.setRecentFeeling(EEmotion.SAD);
        } else if (feeling.equals(EEmotion.ANGRY.name())) {
            userRecommendationElements.setRecentFeeling(EEmotion.ANGRY);
        } else {
            userRecommendationElements.setRecentFeeling(EEmotion.BLANK);
        }

        return true;
    }

    private boolean updateAbvLimit(String username, String abvLimit) {
        UserRecommendationElements userRecommendationElements;

        try {
            userRecommendationElements = getUserRecommendElements(username);
        } catch (Exception e) {
            log.error("updateAbvLimit error : {}", e.getMessage());
            return false;
        }

        userRecommendationElements.setAbvLimit(Short.parseShort(abvLimit));

        return true;
    }

    private boolean updatePreferredCategory(String username, String preferredCategory) {
        UserRecommendationElements userRecommendationElements;

        try {
            userRecommendationElements = getUserRecommendElements(username);
        } catch (Exception e) {
            log.error("updatePreferredCategory error : {}", e.getMessage());
            return false;
        }

        if (preferredCategory.equals(ECategory.WINE.name())) {
            userRecommendationElements.setPreferredCategory(ECategory.WINE);
        } else if (preferredCategory.equals(ECategory.COCKTAIL.name())) {
            userRecommendationElements.setPreferredCategory(ECategory.COCKTAIL);
        } else if (preferredCategory.equals(ECategory.TRADITIONAL_LIQUOR.name())) {
            userRecommendationElements.setPreferredCategory(ECategory.TRADITIONAL_LIQUOR);
        } else if (preferredCategory.equals(ECategory.WHISKY.name())) {
            userRecommendationElements.setPreferredCategory(ECategory.WHISKY);
        } else if (preferredCategory.equals(ECategory.NON_ALCOHOL.name())) {
            userRecommendationElements.setPreferredCategory(ECategory.NON_ALCOHOL);
        } else {
            userRecommendationElements.setPreferredCategory(ECategory.ALL);
        }

        return true;
    }
}
