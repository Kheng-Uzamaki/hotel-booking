package com.kheng.hotelbooking.entity;

import com.kheng.hotelbooking.enums.BookingStatus;
import com.kheng.hotelbooking.enums.PaymentGateway;
import com.kheng.hotelbooking.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String transactionId;

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    PaymentGateway paymentGateway;

    LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    String bookingReference;
    String failureReason;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
