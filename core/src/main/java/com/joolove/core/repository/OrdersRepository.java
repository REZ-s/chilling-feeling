package com.joolove.core.repository;

import com.joolove.core.domain.billing.Orders;
import com.joolove.core.dto.query.BestSeller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, UUID> {

    @Query("select o " +
            "from Orders o " +
            "where o.user.id = ?1")
    public Orders findByUserId(UUID userId);

    @Query("select new com.joolove.core.dto.query.BestSeller(og.goods.id, count(og.goods)) " +
            "from Orders o " +
            "join o.ordersGoodsList og " +
            "where o.orderStatus = 2 " +
            "and o.updatedDate between :nowDate and :beforeDate " +
            "group by og.goods.id")
    public List<BestSeller> findBestSeller(LocalDateTime nowDate, LocalDateTime beforeDate, Pageable pageable);

}
