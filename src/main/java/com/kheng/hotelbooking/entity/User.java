package com.kheng.hotelbooking.entity;

import com.kheng.hotelbooking.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Email is required!")
    @Column(unique = true, nullable = false)
    String email;

    @NotBlank(message = "Password is required!")
    String password;
    String firstName;
    String lastName;

    @NotBlank(message = "Phone_Number is required!")
    @Column(name = "phone_number")
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    UserRole role;

    Boolean isActive;
    final LocalDateTime createdAt = LocalDateTime.now();

}
