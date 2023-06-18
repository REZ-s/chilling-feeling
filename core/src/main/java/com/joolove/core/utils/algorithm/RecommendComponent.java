package com.joolove.core.utils.algorithm;

import com.joolove.core.domain.ECategory;
import com.joolove.core.domain.EEmotion;
import com.joolove.core.domain.EFigure;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.recommend.UserRecommendationBase;
import com.joolove.core.domain.recommend.UserRecommendationDaily;
import com.joolove.core.dto.request.UserRecommendElements;
import com.joolove.core.repository.UserRecommendationBaseRepository;
import com.joolove.core.repository.UserRecommendationDailyRepository;
import com.joolove.core.security.service.GoodsService;
import com.joolove.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RecommendComponent {
    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserRecommendationBaseRepository userRecommendationBaseRepository;
    @Autowired
    private UserRecommendationDailyRepository userRecommendationDailyRepository;

    Map<String, UserRecommendElements> personalMap;  // 개인 추천을 위한 맵 (key : username, value : recommendElements)
    List<String> allRecommendElementsList;         // 추천 요소 리스트 (0 : abvLimit, 1 : preferredCategory, 2 : recentFeeling, 3 : popularGoodsList, 4 : newGoodsList)
    Goods userSelectedGoods;


    // 사용자가 처음 회원가입을 할 때 '기본 추천' 을 진행하거나 마이페이지에서 직접 설정을 바꿀 때 호출된다.
    public boolean setBaseRecommend(String username) {
        try {
            UserRecommendElements userRecommendElements = getUserRecommendElements(username);   // 개인 추천 관련 데이터 가져오기
            personalMap.put(username, userRecommendElements);        // 개인 추천 알고리즘 업데이트
            allRecommendElementsList = combineRecommendElements(userRecommendElements);    // 추천 요소 리스트
        } catch (Exception e) {
            log.error("setBaseRecommend error : {}", e.getMessage());
            return false;
        }

        return true;
    }

    // 사용자가 '오늘의 추천' 을 진행하면 호출된다.
    public boolean setDailyRecommend(String username) {


        return true;
    }

    // 사용자가 어떤 상품을 직접 '클릭' 하거나 '검색' 할 때 호출된다.
    /*
    public boolean setUpdateRealtimeRecommend(String username) {
        if (event.active) {
            userSelectedGoods = getSelectedGoods(userRecommendElements);
            personalMap.put(username, updateRealTimeRecommend(personalMap.get(username), userSelectedGoods));     // 실시간 추천 알고리즘 적용
        }
    }*/

    // 기존에 저장해두었던 사용자 (User) 의 추천 관련 데이터 (DTO : UserRecommendElements) 를 가져온다.
    private UserRecommendElements getUserRecommendElements(String username)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

        UserRecommendationBase userRecommendationBase = userRecommendationBaseRepository.findByUser(user);
        Short abvLimit = 100;
        ECategory preferredCategory = ECategory.All;
        if (userRecommendationBase != null) {
            abvLimit = userRecommendationBase.getAbvLimit();
            preferredCategory = userRecommendationBase.getPreferredCategory();
        }

        UserRecommendationDaily userRecommendationDaily = userRecommendationDailyRepository.findByUser(user);
        EEmotion dailyFeeling = EEmotion.BLANK;
        if (userRecommendationDaily != null) {
            dailyFeeling = userRecommendationDaily.getFeeling();
        }

        return UserRecommendElements.builder()
                .abvLimit(abvLimit)
                .preferredCategory(preferredCategory)
                .recentFeeling(dailyFeeling)
                .build();
    }

    private List<String> combineRecommendElements(UserRecommendElements userRecommendElements) {
        List<String> userRecommendElementsList = new ArrayList<>();

        // 개인별 추천 요소
        userRecommendElementsList.set(0, getDegreeLimit(userRecommendElements));        // 기본 추천
        userRecommendElementsList.set(1, getPreferredCategory(userRecommendElements));  // 기본 추천
        userRecommendElementsList.set(2, getDailyFeeling(userRecommendElements));       // 오늘의 추천

        // 독립적인 추천 요소
        userRecommendElementsList.set(3, getPopularGoodsList());
        userRecommendElementsList.set(4, getNewGoodsList());

        return userRecommendElementsList;
    }

    private String getNewGoodsList() {
        // Goods 테이블 에서 timestamp(created) 를 기준으로 최신 상품 리스트를 가져온다.
        return null;
    }

    private String getPopularGoodsList() {
        // 사용자 행동 데이터를 기반으로 인기 상품 리스트를 가져온다.
        // UserActivityLog 테이블에 필요한 칼럼들을 정의하고 추가해야 이 작업이 가능하네.
        return null;
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

    private String updateDailyFeeling(UserRecommendElements userRecommendElements) {
        String mySweetness = EFigure.UNKNOWN.name();

        if (userRecommendElements.getRecentFeeling().equals(EEmotion.SMILE)
                || userRecommendElements.getRecentFeeling().equals(EEmotion.HAPPY)) {
            mySweetness = EFigure.HIGH.name();
        } else if (userRecommendElements.getRecentFeeling().equals(EEmotion.SAD)
                || userRecommendElements.getRecentFeeling().equals(EEmotion.ANGRY)) {
            mySweetness = EFigure.LOW.name();
        }

        return mySweetness;
    }

}
