package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.EventConstants;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.book.BookRequestDto;
import com.example.librarymanagement.domain.dto.request.receipt.ImportReceiptRequestDto;
import com.example.librarymanagement.domain.dto.response.receipt.ImportReceiptResponseDto;
import com.example.librarymanagement.domain.entity.Book;
import com.example.librarymanagement.domain.entity.BookDefinition;
import com.example.librarymanagement.domain.entity.ImportReceipt;
import com.example.librarymanagement.domain.mapper.ImportReceiptMapper;
import com.example.librarymanagement.domain.specification.ImportReceiptSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ConflictException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.BookDefinitionRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.ImportReceiptRepository;
import com.example.librarymanagement.service.ImportReceiptService;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImportReceiptServiceImpl implements ImportReceiptService {

    private static final String TAG = "Nhập sách";

    private final ImportReceiptRepository importReceiptRepository;

    private final BookDefinitionRepository bookDefinitionRepository;

    private final BookRepository bookRepository;

    private final ImportReceiptMapper importReceiptMapper;

    private final MessageSource messageSource;

    private final LogService logService;

    private ImportReceipt getEntity(Long id) {
        return importReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ImportReceipt.ERR_NOT_FOUND_ID, id));
    }

    private void handleBook(ImportReceiptRequestDto requestDto, ImportReceipt importReceipt) {
        importReceipt.setBooks(new ArrayList<>());

        Map<BookDefinition, Integer> bookDefinitionMap = new HashMap<>();

        for (BookRequestDto bookRequestDto : requestDto.getBookRequestDtos()) {
            BookDefinition bookDefinition = bookDefinitionRepository.findByIdAndActiveFlagIsTrue(bookRequestDto.getBookDefinitionId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, bookRequestDto.getBookDefinitionId()));

            bookDefinitionMap.put(bookDefinition,bookRequestDto.getQuantity());
        }

        for (Map.Entry<BookDefinition, Integer> entry : bookDefinitionMap.entrySet()) {
            BookDefinition bookDefinition = entry.getKey();
            Integer quantity = entry.getValue();

            long currentCount = bookRepository.countByBookDefinitionId(bookDefinition.getId());

            //Lặp qua số lượng tạo sách tương ứng
            for (int i = 0; i < quantity; i++) {
                String bookCode = String.format("%s.%05d", bookDefinition.getBookNumber(), currentCount + i + 1);

                //Tao sach theo bien muc
                Book book = new Book();
                book.setBookCode(bookCode);
                book.setBookDefinition(bookDefinition);
                book.setImportReceipt(importReceipt);

                //Luu vao phieu nhap
                importReceipt.getBooks().add(book);
            }
        }
    }
    @Override
    public String generateReceiptNumber() {
        long currentCount = importReceiptRepository.count();
        long nextNumber = currentCount + 1;
        return String.format("PN%05d", nextNumber);
    }

    @Override
    public CommonResponseDto save(ImportReceiptRequestDto requestDto, String userId) {
        //Kiểm tra trùng số phiếu nhập
        if (importReceiptRepository.existsByReceiptNumber(requestDto.getReceiptNumber())) {
            throw new ConflictException(ErrorMessage.ImportReceipt.ERR_DUPLICATE_NUMBER, requestDto.getReceiptNumber());
        }

        //Tạo phiếu nhập
        ImportReceipt importReceipt = importReceiptMapper.toImportReceipt(requestDto);

        //Xử lý danh sách sách trong phiếu nhập
        handleBook(requestDto, importReceipt);

        //Lưu phiếu nhập vào cơ sở dữ liệu
        importReceiptRepository.save(importReceipt);

        logService.createLog(TAG, EventConstants.ADD, "Tạo phiếu nhập mới: " + importReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new ImportReceiptResponseDto(importReceipt));
    }

    @Override
    @Transactional
    public CommonResponseDto update(Long id, ImportReceiptRequestDto requestDto, String userId) {
        ImportReceipt importReceipt = getEntity(id);

        //Kiểm tra trùng số phiếu nhập
        if (!importReceipt.getReceiptNumber().equals(requestDto.getReceiptNumber()) &&
                importReceiptRepository.existsByReceiptNumber(requestDto.getReceiptNumber())) {
            throw new ConflictException(ErrorMessage.ImportReceipt.ERR_DUPLICATE_NUMBER, requestDto.getReceiptNumber());
        }

        //Cập nhật thông tin phiếu nhập
        importReceipt.setReceiptNumber(requestDto.getReceiptNumber());
        importReceipt.setImportDate(requestDto.getImportDate());
        importReceipt.setGeneralRecordNumber(requestDto.getGeneralRecordNumber());
        importReceipt.setFundingSource(requestDto.getFundingSource());
        importReceipt.setImportReason(requestDto.getImportReason());

        //Xử lý danh sách sách trong phiếu nhập
        List<Book> existingBooks = importReceipt.getBooks();
        for (Book book : existingBooks) {
            if (!book.getBookBorrows().isEmpty() || book.getExportReceipt() != null) {
                throw new BadRequestException(ErrorMessage.Book.ERR_HAS_LINKED, book.getBookCode());
            }
        }

        //Xóa các sách cũ trong phiếu nhập
        bookRepository.deleteAll(existingBooks);

        //Tạo danh sách sách mới
        handleBook(requestDto, importReceipt);

        //Lưu thay đổi
        importReceiptRepository.save(importReceipt);

        //Ghi log cập nhật
        logService.createLog(TAG, "Cập nhật", "Cập nhật phiếu nhập: " + importReceipt.getReceiptNumber(), userId);

        //Trả về kết quả thành công
        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new ImportReceiptResponseDto(importReceipt));
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        ImportReceipt importReceipt = getEntity(id);

        //Kiểm tra sách đã phát sinh dữ liệu hay chưa
        List<Book> books = importReceipt.getBooks();
        for (Book book : books) {
            if (!book.getBookBorrows().isEmpty() || book.getExportReceipt() != null) {
                throw new BadRequestException(ErrorMessage.Book.ERR_HAS_LINKED, book.getBookCode());
            }
        }

        importReceiptRepository.delete(importReceipt);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa phiếu nhập: " + importReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<ImportReceiptResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.IMPORT_RECEIPT);

        Page<ImportReceipt> page = importReceiptRepository.findAll(
                ImportReceiptSpecification.filterImportReceipts(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<ImportReceiptResponseDto> items = page.getContent().stream()
                .map(ImportReceiptResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.IMPORT_RECEIPT, page);

        PaginationResponseDto<ImportReceiptResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public ImportReceiptResponseDto findById(Long id) {
        ImportReceipt importReceipt = getEntity(id);
        return new ImportReceiptResponseDto(importReceipt);
    }
}
