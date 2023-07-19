package com.joolove.core.repository;

import com.joolove.core.domain.product.GoodsStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoodsStatsRepository extends JpaRepository<GoodsStats, UUID> {

    GoodsStats findOneById(UUID id);
}
