package com.kheng.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kheng.hotelbooking.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    // generic
    int status;
    String message;

    // for login
    String token;
    UserRole role;
    Boolean isActive;
    String expirationTime;

    // user Data Output
    UserDTO user;
    List<UserDTO> users;

    // booking Data Output
    BookingDTO booking;
    List<BookingDTO> bookings;

    // room Data Output
    RoomDTO room;
    List<RoomDTO> rooms;

    // payment Data Output
    PaymentDTO payment;
    List<PaymentDTO> payments;

    // Notification Data Output
    NotificationDTO notification;
    List<NotificationDTO> notifications;

    final LocalDateTime timestamp = LocalDateTime.now();
}
