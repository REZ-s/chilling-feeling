package com.joolove.core.repository;

import com.joolove.core.domain.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // @OneToOne 관계에 있는 엔티티들을 한꺼번에 가져온다. (Lazy Loading 적용 대상이 아니기 때문에)
    @Query("SELECT u " +
            "FROM User u " +
            "JOIN FETCH u.socialLogin " +
            "JOIN FETCH u.password " +
            "JOIN FETCH u.cidi " +
            "JOIN FETCH u.profile " +
            "JOIN FETCH u.favorite " +
            "JOIN FETCH u.userPersonal " +
            "JOIN FETCH u.userRecommendationBase " +
            "JOIN FETCH u.userRecommendationDaily " +
            "JOIN FETCH u.refreshToken " +
            "WHERE u.username = :username")
    User findByUsernameWithRelations(@Param("username") String username);

    User findByUsername(String username);

    @Query("select new User(u.id, u.username, u.accountType) from User u")
    List<User> findAllFast();

    Boolean existsByUsername(String username);

}
