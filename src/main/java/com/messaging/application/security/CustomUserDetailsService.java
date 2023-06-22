package com.messaging.application.security;

import com.messaging.application.models.User;
import com.messaging.application.service.MessagingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MessagingService messagingService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = messagingService.queryUserByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User not found: " + username);

        String role = user.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER";
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                new BCryptPasswordEncoder().encode(user.getPassword()),
                authorities
        );
    }
}