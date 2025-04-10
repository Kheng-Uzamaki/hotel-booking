package com.kheng.hotelbooking.entity;

import com.kheng.hotelbooking.enums.BookingStatus;
import com.kheng.hotelbooking.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    LocalDate checkInDate;
    LocalDate checkOutDate;
    BigDecimal totalPrice;
    String bookingReference;
    LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;
}
