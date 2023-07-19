package com.joolove.core.repository;

import com.joolove.core.domain.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // @OneToOne 관계에 있는 엔티티들을 한번에 가져온다. (Eager Loading 적용 대상)
    @Query("SELECT u " +
            "FROM User u " +
            "LEFT JOIN FETCH u.role " +
            "LEFT JOIN FETCH u.socialLogin " +
            "LEFT JOIN FETCH u.password " +
            "LEFT JOIN FETCH u.cidi " +
            "LEFT JOIN FETCH u.profile " +
            "LEFT JOIN FETCH u.favorite " +
            "LEFT JOIN FETCH u.userPersonal " +
            "LEFT JOIN FETCH u.userRecommendationBase " +
            "LEFT JOIN FETCH u.userRecommendationDaily " +
            "WHERE u.username = :username")
    User findByUsernameWithRelations(@Param("username") String username);

    User findByUsername(String username);

}
