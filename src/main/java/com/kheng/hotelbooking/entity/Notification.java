package com.kheng.hotelbooking.entity;

import com.kheng.hotelbooking.enums.BookingStatus;
import com.kheng.hotelbooking.enums.NotificationType;
import com.kheng.hotelbooking.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String subject;

    @NotBlank(message = "recipient is required!")
    String recipient;

    String body;

    @Enumerated(EnumType.STRING)
    NotificationType type;

    String bookingReference;
    LocalDateTime createdAt = LocalDateTime.now();

}
