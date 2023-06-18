package com.joolove.core.repository;


import com.joolove.core.domain.member.User;
import com.joolove.core.domain.recommend.UserRecommendationDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRecommendationDailyRepository extends JpaRepository<UserRecommendationDaily, UUID> {

    UserRecommendationDaily findByUser(User user);
}
