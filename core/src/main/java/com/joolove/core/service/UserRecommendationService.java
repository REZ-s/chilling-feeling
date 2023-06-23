package com.joolove.core.service;

import com.joolove.core.domain.member.User;
import com.joolove.core.domain.recommendation.UserRecommendationBase;
import com.joolove.core.domain.recommendation.UserRecommendationDaily;
import com.joolove.core.repository.UserRecommendationBaseRepository;
import com.joolove.core.repository.UserRecommendationDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRecommendationService {

    private final UserRecommendationBaseRepository userRecommendationBaseRepository;

    private final UserRecommendationDailyRepository userRecommendationDailyRepository;

    @Transactional
    public void addUserRecommendationBase(UserRecommendationBase userRecommendationBase) {

        // 아래에 save 메소드로 update 를 했는데, 만약에 기존에 있던 데이터를 update 하는게 아닌
        // 새로 create 하는 것이라면 수정해야 함.
        userRecommendationBaseRepository.save(userRecommendationBase);
    }

    @Transactional
    public void addUserRecommendationDaily(UserRecommendationDaily userRecommendationDaily) {

        // 아래에 save 메소드로 update 를 했는데, 만약에 기존에 있던 데이터를 update 하는게 아닌
        // 새로 create 하는 것이라면 수정해야 함.
        userRecommendationDailyRepository.save(userRecommendationDaily);
    }

    public UserRecommendationBase findUserRecommendationBase(User user) {
        return userRecommendationBaseRepository.findByUser(user);
    }

    public UserRecommendationDaily findUserRecommendationDaily(User user) {
        return userRecommendationDailyRepository.findByUser(user);
    }
}
