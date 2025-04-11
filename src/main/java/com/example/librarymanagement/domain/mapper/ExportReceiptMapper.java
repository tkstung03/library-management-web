package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.receipt.ExportReceiptRequestDto;
import com.example.librarymanagement.domain.entity.ExportReceipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExportReceiptMapper {
    ExportReceipt toExportReceipt(ExportReceiptRequestDto requestDto);
}
