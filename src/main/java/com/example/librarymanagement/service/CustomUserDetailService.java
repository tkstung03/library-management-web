package com.example.librarymanagement.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailService {

    UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException;

    UserDetails loadUserByCartNumber(String cardNumber) throws UsernameNotFoundException;

}
