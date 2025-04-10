package com.kheng.hotelbooking.repository;

import com.kheng.hotelbooking.entity.Notification;
import com.kheng.hotelbooking.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
