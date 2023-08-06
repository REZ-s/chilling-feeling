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
        userRecommendationBaseRepository.save(userRecommendationBase);
    }

    @Transactional
    public void addUserRecommendationDaily(UserRecommendationDaily userRecommendationDaily) {
        userRecommendationDailyRepository.save(userRecommendationDaily);
    }

    @Transactional
    public void updateUserRecommendationBase(UserRecommendationBase userRecommendationBase) {
        UserRecommendationBase recommendationBase = userRecommendationBaseRepository.findByUser(userRecommendationBase.getUser());
        recommendationBase.setAbvLimit(userRecommendationBase.getAbvLimit());
        recommendationBase.setPreferredCategories(userRecommendationBase.getPreferredCategories());
        userRecommendationBaseRepository.save(recommendationBase);
    }

    @Transactional
    public void updateUserRecommendationDaily(UserRecommendationDaily userRecommendationDaily) {
        UserRecommendationDaily recommendationDaily = userRecommendationDailyRepository.findByUser(userRecommendationDaily.getUser());
        recommendationDaily.setFeeling(userRecommendationDaily.getFeeling());
        userRecommendationDailyRepository.save(recommendationDaily);
    }

    public UserRecommendationBase findUserRecommendationBase(User user) {
        return userRecommendationBaseRepository.findByUser(user);
    }

    public UserRecommendationDaily findUserRecommendationDaily(User user) {
        return userRecommendationDailyRepository.findByUser(user);
    }

}
