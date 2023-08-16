package com.joolove.core.service;

import com.joolove.core.domain.billing.Orders;
import com.joolove.core.domain.billing.Payment;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.OrdersGoods;
import com.joolove.core.dto.query.BestSeller;
import com.joolove.core.dto.query.GoodsViewDetails;
import com.joolove.core.repository.OrdersGoodsRepository;
import com.joolove.core.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrdersGoodsRepository ordersGoodsRepository;
    private final PaymentService paymentService;
    private final GoodsService goodsService;

    public Orders findByUserId(UUID userId) {
        return ordersRepository.findByUserId(userId);
    }

    // 최고 판매량 상품 조회 (날짜별)
    public Map<String, Object> getBestSellerGoodsByDate(short days) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(days);

        List<BestSeller> ordersList = ordersRepository.findBestSeller(beforeDate, (Pageable) PageRequest.of(0, 1));
        if (ordersList == null || ordersList.size() == 0) {
            return new HashMap<>();
        }

        BestSeller bestSeller = ordersList.get(0);

        UUID goodsId = bestSeller.getGoodsId();
        Long salesCount = bestSeller.getSalesCount();

        GoodsViewDetails goodsViewDetails = goodsService.findGoodsByGoodsId(goodsId);

        Map<String, Object> result = new HashMap<>();
        result.put("goodsViewDetails", goodsViewDetails);
        result.put("salesCount", salesCount);
        return result;
    }

    // 주문하기
    @Transactional
    public Orders createOrder(User user, Goods goods, int inputCount) {
        Orders order = Orders.builder()
                .user(user)
                .orderStatus((short)1)
                .build();
        ordersRepository.save(order);

        OrdersGoods ordersGoods = OrdersGoods.builder()
                .goods(goods)
                .order(order)
                .count(inputCount)
                .singleSalePrice(goodsService.getSingleSalePrice(goods.getName()))
                .build();
        ordersGoodsRepository.save(ordersGoods);

        Payment payment = paymentService.createPayment(order);

        List<OrdersGoods> ordersGoodsList = order.getOrdersGoodsList();
        if (ordersGoodsList == null) {
            ordersGoodsList = new ArrayList<>();
        }
        ordersGoodsList.add(ordersGoods);

        order.setOrdersGoodsList(ordersGoodsList);
        order.setPayment(payment);

        return order;
    }

    // 주문 완료 (결제 및 배송까지 끝난 상황. 사용자가 수령확인 버튼을 누르거나 관리자가 처리)
    @Transactional
    public Orders completeOrder(Orders order) {
        order.updateOrderStatus((short) 2);

        // 주문에 포함된 모든 상품들의 판매량을 증가시킨다.
        List<OrdersGoods> ordersGoodsList = order.getOrdersGoodsList();
        for (OrdersGoods og : ordersGoodsList) {
            completeOrderGoods(og);
        }

        return order;
    }

    @Transactional
    public void completeOrderGoods(OrdersGoods ordersGoods) {
        Goods goods = ordersGoods.getGoods();
        goods.updateSalesFigures(goods.getSalesFigures() + ordersGoods.getCount().shortValue());
    }
}
