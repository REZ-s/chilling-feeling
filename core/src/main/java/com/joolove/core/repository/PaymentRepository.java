package com.joolove.core.repository;

import com.joolove.core.domain.billing.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    public Payment findByOrderId(UUID orderId);
}
