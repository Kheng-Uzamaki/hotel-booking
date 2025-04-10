package com.kheng.hotelbooking.repository;

import com.kheng.hotelbooking.entity.BookingReference;
import com.kheng.hotelbooking.entity.Notification;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingReferenceRepository extends JpaRepository<BookingReference,Long> {
    Optional<BookingReference> findByReferenceNo(String referenceNo);
}
