package com.joolove.core.repository;

import com.joolove.core.domain.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // @OneToOne 관계에 있는 엔티티들을 한번에 가져온다. (Eager Loading 적용 대상)
    @Query("select distinct u " +
            "from User u " +
            "left join fetch u.role " +
            "left join fetch u.socialLogin " +
            "left join fetch u.password " +
            "left join fetch u.cidi " +
            "left join fetch u.profile " +
            "left join fetch u.favorite " +
            "left join fetch u.userPersonal " +
            "left join fetch u.userRecommendationBase " +
            "left join fetch u.userRecommendationDaily " +
            "where u.username = ?1")
    User findByUsernameWithRelations(String username);

    User findByUsername(String username);
}
