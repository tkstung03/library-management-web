package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.response.cart.CartDetailResponseDto;

import java.util.List;
import java.util.Set;

public interface CartService {

    List<CartDetailResponseDto> getCartDetails(String cardNumber);

    CommonResponseDto addToCart(String cardNumber, String bookCode);

    CommonResponseDto removeFromCart(String cardNumber, Set<Long> cartDetailIds);

    CommonResponseDto clearCart(String cardNumber);
}
