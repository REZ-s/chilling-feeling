package com.joolove.core.repository;

import com.joolove.core.domain.billing.Orders;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.OrdersGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Repository
public interface OrdersGoodsRepository extends JpaRepository<OrdersGoods, UUID>  {

}
