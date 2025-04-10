package com.kheng.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kheng.hotelbooking.entity.Room;
import com.kheng.hotelbooking.entity.User;
import com.kheng.hotelbooking.enums.BookingStatus;
import com.kheng.hotelbooking.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDTO {
    Long id;

    UserDTO user;

    RoomDTO room;
    Long roomId;

    PaymentStatus paymentStatus;

    LocalDate checkInDate;
    LocalDate checkOutDate;
    BigDecimal totalPrice;
    String bookingReference;
    LocalDateTime createdAt;

    BookingStatus bookingStatus;
}
