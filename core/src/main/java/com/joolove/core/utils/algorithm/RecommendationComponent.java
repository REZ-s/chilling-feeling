package com.joolove.core.utils.algorithm;

import com.joolove.core.domain.ECategory;
import com.joolove.core.domain.EEmotion;
import com.joolove.core.domain.ETargetCode;
import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.recommend.UserRecommendationBase;
import com.joolove.core.domain.recommend.UserRecommendationDaily;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityElements;
import com.joolove.core.dto.request.UserRecommendElements;
import com.joolove.core.service.GoodsService;
import com.joolove.core.service.UserActivityService;
import com.joolove.core.service.UserRecommendationService;
import com.joolove.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

    // 사용자의 행동 패턴이 변한 것이 없다면, 기존에 저장된 추천 요소를 가져온다. (서비스 진입 시, run 호출 후 저장됨)
    private List<Object> allRecommendElementsList;         // 추천 요소 리스트 (0 : abvLimit, 1 : preferredCategory, 2 : recentFeeling, 3 : popularGoodsList, 4 : newGoodsList, 5 : )

    // 실시간 추천
    // '최종 상품 추천 리스트' = run('기본 추천' + '오늘의 추천' + '최근 사용자 행동 데이터 요소 5개')
    public List<Object> run(String username) {
        UserRecommendElements userRecommendElements = getUserRecommendElements(username);   // 개인 추천 관련 데이터 가져오기
        List<UserActivityElements> userActivityElementsList = getUserActivityElements(username);    // 사용자 행동 데이터 가져오기

        allRecommendElementsList = combineRecommendElements(userRecommendElements, userActivityElementsList);    // 모든 추천 요소 리스트
        return allRecommendElementsList;
    }

    // 사용자의 '기본 추천' 과 '오늘의 추천' 을 설정한다.
    public boolean setUserRecommendation(String username, UserRecommendElements userRecommendElements) {
        try {
            updateUserRecommendElements(username, userRecommendElements);
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

    private void updateUserRecommendElements(String username, UserRecommendElements userRecommendElements)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

        UserRecommendationBase userRecommendationBase = UserRecommendationBase.builder()
                    .user(user)
                    .abvLimit(userRecommendElements.getAbvLimit())
                    .preferredCategory(userRecommendElements.getPreferredCategory())
                    .build();

        UserRecommendationDaily userRecommendationDaily = UserRecommendationDaily.builder()
                    .user(user)
                    .feeling(userRecommendElements.getRecentFeeling())
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
    private UserRecommendElements getUserRecommendElements(String username)
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

        return UserRecommendElements.builder()
                .username(username)
                .abvLimit(abvLimit)
                .preferredCategory(preferredCategory)
                .recentFeeling(dailyFeeling)
                .build();
    }

    // 모든 추천 요소들을 하나의 리스트에 담는다.
    private List<Object> combineRecommendElements(UserRecommendElements userRecommendElements, List<UserActivityElements> userActivityElements) {
        List<Object> userRecommendElementsList = new ArrayList<>();

        // 개인별 추천 요소
        userRecommendElementsList.add(getDegreeLimit(userRecommendElements));        // 기본 추천
        userRecommendElementsList.add(getPreferredCategory(userRecommendElements));  // 기본 추천
        userRecommendElementsList.add(getDailyFeeling(userRecommendElements));       // 오늘의 추천

        // 독립적인 추천 요소
        userRecommendElementsList.add(getPopularGoodsList());
        userRecommendElementsList.add(getNewGoodsList());

        // 사용자 행동 데이터 기반 추천 요소
        for (UserActivityElements e : userActivityElements) {
            userRecommendElementsList.add(e.getTargetName());
        }

        return userRecommendElementsList;
    }

    // 최신 상품 리스트를 가져온다.
    private List<IGoodsView> getNewGoodsList() {
        return goodsService.findNewGoodsListDefault();
    }

    // 인기 상품 리스트를 가져온다.
    private List<IGoodsView> getPopularGoodsList() {
        return userActivityService.findBestViewsUserActivityListDefault();
    }

    private String getDailyFeeling(UserRecommendElements userRecommendElements) {
        return userRecommendElements.getRecentFeeling().toString();
    }

    private String getPreferredCategory(UserRecommendElements userRecommendElements) {
        return userRecommendElements.getPreferredCategory().toString();
    }

    private String getDegreeLimit(UserRecommendElements userRecommendElements) {
        return userRecommendElements.getAbvLimit().toString();
    }

    private boolean updateDailyFeeling(String username, String feeling) {
        UserRecommendElements userRecommendElements;

        try {
            userRecommendElements = getUserRecommendElements(username);
        } catch (Exception e) {
            log.error("updateDailyFeeling error : {}", e.getMessage());
            return false;
        }

        if (feeling.equals(EEmotion.SMILE.name())) {
            userRecommendElements.setRecentFeeling(EEmotion.SMILE);
        } else if (feeling.equals(EEmotion.HAPPY.name())) {
            userRecommendElements.setRecentFeeling(EEmotion.HAPPY);
        } else if (feeling.equals(EEmotion.SAD.name())) {
            userRecommendElements.setRecentFeeling(EEmotion.SAD);
        } else if (feeling.equals(EEmotion.ANGRY.name())) {
            userRecommendElements.setRecentFeeling(EEmotion.ANGRY);
        } else {
            userRecommendElements.setRecentFeeling(EEmotion.BLANK);
        }

        return true;
    }

    private boolean updateAbvLimit(String username, String abvLimit) {
        UserRecommendElements userRecommendElements;

        try {
            userRecommendElements = getUserRecommendElements(username);
        } catch (Exception e) {
            log.error("updateAbvLimit error : {}", e.getMessage());
            return false;
        }

        userRecommendElements.setAbvLimit(Short.parseShort(abvLimit));

        return true;
    }

    private boolean updatePreferredCategory(String username, String preferredCategory) {
        UserRecommendElements userRecommendElements;

        try {
            userRecommendElements = getUserRecommendElements(username);
        } catch (Exception e) {
            log.error("updatePreferredCategory error : {}", e.getMessage());
            return false;
        }

        if (preferredCategory.equals(ECategory.WINE.name())) {
            userRecommendElements.setPreferredCategory(ECategory.WINE);
        } else if (preferredCategory.equals(ECategory.COCKTAIL.name())) {
            userRecommendElements.setPreferredCategory(ECategory.COCKTAIL);
        } else if (preferredCategory.equals(ECategory.TRADITIONAL_LIQUOR.name())) {
            userRecommendElements.setPreferredCategory(ECategory.TRADITIONAL_LIQUOR);
        } else if (preferredCategory.equals(ECategory.WHISKY.name())) {
            userRecommendElements.setPreferredCategory(ECategory.WHISKY);
        } else if (preferredCategory.equals(ECategory.NON_ALCOHOL.name())) {
            userRecommendElements.setPreferredCategory(ECategory.NON_ALCOHOL);
        } else {
            userRecommendElements.setPreferredCategory(ECategory.ALL);
        }

        return true;
    }
}
