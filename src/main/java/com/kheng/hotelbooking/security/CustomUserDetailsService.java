package com.kheng.hotelbooking.security;

import com.kheng.hotelbooking.entity.User;
import com.kheng.hotelbooking.exception.NotFoundException;
import com.kheng.hotelbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new NotFoundException("User not found with username: " + username));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}
