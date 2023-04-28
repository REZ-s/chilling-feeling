package com.joolove.core.repository;

import com.joolove.core.domain.product.GoodsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoodsDetailsRepository extends JpaRepository<GoodsDetails, UUID> {

}
