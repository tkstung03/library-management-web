package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.receipt.ImportReceiptRequestDto;
import com.example.librarymanagement.domain.entity.ImportReceipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImportReceiptMapper {
    ImportReceipt toImportReceipt (ImportReceiptRequestDto requestDto);
}
