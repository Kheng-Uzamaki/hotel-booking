package com.kheng.hotelbooking.repository;

import com.kheng.hotelbooking.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {
}
