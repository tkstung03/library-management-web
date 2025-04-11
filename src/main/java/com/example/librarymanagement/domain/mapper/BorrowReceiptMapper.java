package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.receipt.BorrowReceiptRequestDto;
import com.example.librarymanagement.domain.entity.BorrowReceipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowReceiptMapper {

    BorrowReceipt toBorrowReceipt(BorrowReceiptRequestDto requestDto);

}
