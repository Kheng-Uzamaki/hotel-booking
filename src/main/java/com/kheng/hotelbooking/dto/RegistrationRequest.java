package com.kheng.hotelbooking.dto;

import com.kheng.hotelbooking.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    @NotBlank(message = "FirstName is required!")
    String firstName;

    @NotBlank(message = "LastName is required!")
    String lastName;

    @NotBlank(message = "Phone Number is required!")
    String phoneNumber;

    UserRole role;

    @NotBlank(message = "Email is required!")
    String email;

    @NotBlank(message = "Password is required!")
    String password;
}
