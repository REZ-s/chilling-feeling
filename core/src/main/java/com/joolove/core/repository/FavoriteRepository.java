package com.joolove.core.repository;

import com.joolove.core.domain.billing.Cart;
import com.joolove.core.domain.member.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    @Query("select f " +
            "from Favorite f " +
            "where f.user.id = ?1 ")
    public Favorite findByUserId(UUID userId);
}
