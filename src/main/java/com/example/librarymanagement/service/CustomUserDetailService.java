package com.example.librarymanagement.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailService extends UserDetailsService {

    UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException;

    UserDetails loadUserByCardNumber(String cardNumber) throws UsernameNotFoundException;

}
