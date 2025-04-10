package com.kheng.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kheng.hotelbooking.entity.User;
import com.kheng.hotelbooking.enums.PaymentGateway;
import com.kheng.hotelbooking.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO {

    Long id;
    BookingDTO booking;

    String transactionId;

    BigDecimal amount;

    PaymentGateway paymentMethod;

    LocalDateTime paymentDate;

    PaymentStatus status;

    String bookingReference;
    String failureReason;

    String approvalLink;
}
