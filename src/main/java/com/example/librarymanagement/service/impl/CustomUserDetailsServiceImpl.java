package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.entity.User;
import com.example.librarymanagement.repository.ReaderRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.security.UserDetailsFactory;
import com.example.librarymanagement.service.CustomUserDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsServiceImpl implements CustomUserDetailService {

    UserRepository userRepository;

    ReaderRepository readerRepository;

    MessageSource messageSource;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrCardNumber) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(usernameOrCardNumber).orElse(null);
        if (user != null) {
            return UserDetailsFactory.fromUser(user);
        } else {
            Reader reader = readerRepository.findByCardNumber(usernameOrCardNumber)
                    .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER,
                            new String[]{usernameOrCardNumber}, LocaleContextHolder.getLocale())));

            return UserDetailsFactory.fromReader(reader);
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(ErrorMessage.User.ERR_NOT_FOUND_ID, new String[]{userId}, LocaleContextHolder.getLocale())));

        return UserDetailsFactory.fromUser(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByCartNumber(String cardNumber) throws UsernameNotFoundException {
        Reader reader = readerRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER, new String[]{cardNumber}, LocaleContextHolder.getLocale())));

        return UserDetailsFactory.fromReader(reader);
    }


}
