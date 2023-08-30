package com.joolove.core.service;

import com.joolove.core.domain.billing.Orders;
import com.joolove.core.domain.billing.Payment;
import com.joolove.core.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment findByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    @Transactional
    public Payment createPayment(Orders order) {
        Payment payment = Payment.builder()
                .order(order)
                .paymentType((short)1)
                .paymentStatus((short)1)
                .discountRate((short)0)
                .isRefundable(true)
                .point(0)
                .rawPrice(0)
                .build();

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment completePayment(Orders order) {
        Payment payment = findByOrderId(order.getId());
        // 기능을 확장한다면, Payment Gateway 에 요청을 보내고 정상 응답을 받은 후에 결제완료로 변경한다.
        payment.updatePaymentStatus((short) 2);
        return payment;
    }
}
