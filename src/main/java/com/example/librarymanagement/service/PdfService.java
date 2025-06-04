package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.response.library.LibraryVisitResponseDto;
import com.example.librarymanagement.domain.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface PdfService {
    byte[] createReaderCard(String managementUnit, String schoolName, String principalName, List<Reader> readers);

    byte[] createReceipt(User user, String schoolName, List<BorrowReceipt> borrowReceipts);

    byte[] createReceiptWithFourPerPage(String schoolName);

    byte[] createPdfFromBooks(List<Book> books);

    byte[] createLabelType1Pdf(String librarySymbol, List<Book> books);

    byte[] createLabelType2Pdf(List<Book> books);

    byte[] createBookListPdf(String name, List<Category> categories);

    byte[] createOverdueListPdf(List<BorrowReceipt> borrowReceipts);

    public byte[] generateVisitReportPdf(List<LibraryVisitResponseDto> responseDtos, LocalDate startDate, LocalDate endDate);
}
