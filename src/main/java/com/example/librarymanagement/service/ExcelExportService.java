package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.response.library.LibraryVisitResponseDto;
import com.example.librarymanagement.domain.entity.BorrowReceipt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ExcelExportService {
    byte[] createBorrowingReport(List<BorrowReceipt> borrowReceipts) throws IOException;

    byte[] createLibraryVisitReport(List<LibraryVisitResponseDto> data, LocalDate startDate, LocalDate endDate) throws IOException;

}
