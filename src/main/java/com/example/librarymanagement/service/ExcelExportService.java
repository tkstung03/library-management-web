package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.entity.BorrowReceipt;

import java.io.IOException;
import java.util.List;

public interface ExcelExportService {
    byte[] createBorrowingReport(List<BorrowReceipt> borrowReceipts) throws IOException;

}
