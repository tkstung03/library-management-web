package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.response.cart.CartDetailResponseDto;
import com.example.librarymanagement.domain.dto.response.receipt.BorrowRequestSummaryResponseDto;

import java.util.List;
import java.util.Set;

public interface CartService {

    List<CartDetailResponseDto> getCartDetails(String cardNumber);

    CommonResponseDto addToCart(String cardNumber, Long id);

    CommonResponseDto removeFromCart(String cardNumber, Set<Long> cartDetailIds);

    CommonResponseDto clearCart(String cardNumber);

    PaginationResponseDto<BorrowRequestSummaryResponseDto> getPendingBorrowRequests(PaginationFullRequestDto requestDto);
}
