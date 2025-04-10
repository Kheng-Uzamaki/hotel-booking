package com.kheng.hotelbooking.entity;

import com.kheng.hotelbooking.enums.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Min(value = 1,message = "Room Number must be at leat 1")
    @Column(unique = true)
    Integer roomNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "RoomType is required!")
    @Column(name = "type")
    RoomType type;

    @DecimalMin(value = "0.1",message ="Price per Night is required!")
    BigDecimal pricePerNight;

    @Min(value = 1, message = "Capacity must be at least 1")
    Integer capacity;

    String description;
    String imageUrl;


}
