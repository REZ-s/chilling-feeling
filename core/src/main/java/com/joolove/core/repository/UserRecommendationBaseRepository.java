package com.joolove.core.repository;

import com.joolove.core.domain.member.User;
import com.joolove.core.domain.recommend.UserRecommendationBase;
import com.joolove.core.domain.recommend.UserRecommendationDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRecommendationBaseRepository extends JpaRepository<UserRecommendationBase, UUID> {

    UserRecommendationBase findByUser(User user);

}
