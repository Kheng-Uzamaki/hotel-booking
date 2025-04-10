package com.kheng.hotelbooking.service;

import com.kheng.hotelbooking.dto.LoginRequest;
import com.kheng.hotelbooking.dto.RegistrationRequest;
import com.kheng.hotelbooking.dto.Response;
import com.kheng.hotelbooking.dto.UserDTO;
import com.kheng.hotelbooking.entity.User;

public interface UserService {
    Response registerUser(RegistrationRequest registrationRequest);
    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();
    Response getOwnAccountDetails();
    User getCurrentLoggedInUser();
    Response updateOwnAccount(UserDTO userDTO);
    Response deleteOwnAccount();
    Response getMyBookingHistory();
}
