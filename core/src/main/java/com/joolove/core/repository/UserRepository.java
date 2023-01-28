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

    Optional<User> findByUsername(String username);

    /*
    @Query("select u.username, u.accountType from User u")
    List<Object[]> findAllFastV0();
    */

    @Query("select new User(u.id, u.username, u.accountType) from User u")
    List<User> findAllFast();

    Boolean existsByUsername(String username);

}
