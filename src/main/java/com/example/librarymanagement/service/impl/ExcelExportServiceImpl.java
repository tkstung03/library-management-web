package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.domain.dto.response.library.LibraryVisitResponseDto;
import com.example.librarymanagement.domain.entity.BorrowReceipt;
import com.example.librarymanagement.service.ExcelExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    @Override
    public byte[] createBorrowingReport(List<BorrowReceipt> borrowReceipts) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Sheet1");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Số phiếu", "Mã thẻ", "Bạn đọc", "Loại thẻ", "Ngày mượn", "Ngày hẹn trả", "Số lượng sách", "Trạng thái"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int rowIndex = 1;
        for (BorrowReceipt borrowReceipt : borrowReceipts) {
            Row row = sheet.createRow(rowIndex++);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(borrowReceipt.getReceiptNumber());

            Cell cell1 = row.createCell(1);  // Mã thẻ
            cell1.setCellValue(borrowReceipt.getReader().getCardNumber());

            Cell cell2 = row.createCell(2);  // Bạn đọc
            cell2.setCellValue(borrowReceipt.getReader().getFullName());

            Cell cell3 = row.createCell(3);  // Loại thẻ
            cell3.setCellValue(borrowReceipt.getReader().getCardType().getDisplayName());

            Cell cell4 = row.createCell(4);  // Ngày mượn
            cell4.setCellValue(borrowReceipt.getBorrowDate().format(dateTimeFormatter));

            Cell cell5 = row.createCell(5);  // Ngày hẹn trả
            cell5.setCellValue(borrowReceipt.getDueDate().format(dateTimeFormatter));

            Cell cell6 = row.createCell(6);  // Số lượng sách
            cell6.setCellValue(borrowReceipt.getBookBorrows().size());

            Cell cell7 = row.createCell(7);  // Trạng thái
            cell7.setCellValue(borrowReceipt.getStatus().getName());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    @Override
    public byte[] createLibraryVisitReport(List<LibraryVisitResponseDto> data, LocalDate startDate, LocalDate endDate) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Thống kê");

        int rowIndex = 0;

        // Style cho tiêu đề
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Gộp ô và tạo tiêu đề
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("THỐNG KÊ SỐ LƯỢT VÀO - RA THƯ VIỆN");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        // Dòng ngày tháng
        Row dateRow = sheet.createRow(rowIndex++);
        Cell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Từ ngày: " + startDate + "   Đến ngày: " + endDate);
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setAlignment(HorizontalAlignment.CENTER);
        dateCell.setCellStyle(dateStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));

        rowIndex++; // Dòng trống

        // Header
        String[] headers = {"STT", "Số thẻ", "Họ tên", "Loại thẻ", "Chuyên ngành", "Giờ vào", "Giờ ra"};
        Row headerRow = sheet.createRow(rowIndex++);
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Style cho ô thời gian
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle dateTimeStyle = workbook.createCellStyle();
        dateTimeStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

        int stt = 1;
        for (LibraryVisitResponseDto dto : data) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(stt++);
            row.createCell(1).setCellValue(dto.getCardNumber());
            row.createCell(2).setCellValue(dto.getFullName());
            row.createCell(3).setCellValue(dto.getCardType());
            row.createCell(4).setCellValue(dto.getMajor());

            // Convert LocalDateTime to Date
            if (dto.getEntryTime() != null) {
                Cell entryCell = row.createCell(5);
                entryCell.setCellValue(java.sql.Timestamp.valueOf(dto.getEntryTime()));
                entryCell.setCellStyle(dateTimeStyle);
            }

            if (dto.getExitTime() != null) {
                Cell exitCell = row.createCell(6);
                exitCell.setCellValue(java.sql.Timestamp.valueOf(dto.getExitTime()));
                exitCell.setCellStyle(dateTimeStyle);
            } else {
                row.createCell(6).setCellValue("");
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }


}
