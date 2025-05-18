package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.*;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.receipt.ExportReceiptRequestDto;
import com.example.librarymanagement.domain.dto.response.receipt.ExportReceiptResponseDto;
import com.example.librarymanagement.domain.entity.Book;
import com.example.librarymanagement.domain.entity.ExportReceipt;
import com.example.librarymanagement.domain.mapper.ExportReceiptMapper;
import com.example.librarymanagement.domain.specification.ExportReceiptSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ConflictException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.ExportReceiptRepository;
import com.example.librarymanagement.service.ExportReceiptService;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportReceiptServiceImpl implements ExportReceiptService {

    private static final String TAG = "Xuất sách";

    private final ExportReceiptRepository exportReceiptRepository;

    private final ExportReceiptMapper exportReceiptMapper;

    private final MessageSource messageSource;

    private final LogService logService;

    private final BookRepository bookRepository;

    private ExportReceipt getEntity(Long id) {
        return exportReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ExportReceipt.ERR_NOT_FOUND_ID, id));
    }

    private void getBook(ExportReceipt exportReceipt, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Book.ERR_NOT_FOUND_ID, bookId));

        if (book.getExportReceipt() != null) {
            throw new ConflictException(ErrorMessage.Book.ERR_HAS_LINKED_EXPORT_RECEPTION, book.getBookCode());
        }
        if (book.getBookCondition().equals(BookCondition.ON_LOAN)){
            throw new BadRequestException(ErrorMessage.Book.ERR_BOOK_ALREADY_BORROWED, book.getBookCode());
        }

        book.setExportReceipt(exportReceipt);
        exportReceipt.getBooks().add(book);
    }
    @Override
    public String generateReceiptNumber() {
        long currentNumber = exportReceiptRepository.count();
        long nextNumber = currentNumber + 1;

        return String.format("PX%03d",nextNumber);
    }

    @Override
    public CommonResponseDto save(ExportReceiptRequestDto requestDto, String userId) {
        //Kiem tra trung
        if (exportReceiptRepository.existsByReceiptNumber(requestDto.getReceiptNumber())) {
            throw new ConflictException(ErrorMessage.ExportReceipt.ERR_DUPLICATE_NUMBER, requestDto.getReceiptNumber());
        }

        ExportReceipt exportReceipt = exportReceiptMapper.toExportReceipt(requestDto);

        //Them sach moi vao phieu xuat
        for (Long bookId : requestDto.getBookIds()) {
            getBook(exportReceipt, bookId);
        }

        exportReceiptRepository.save(exportReceipt);

        logService.createLog(TAG, EventConstants.ADD, "Tạo phiếu xuất mới: " + exportReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto update(Long id, ExportReceiptRequestDto requestDto, String userId) {
        ExportReceipt exportReceipt = getEntity(id);

        //Kiem tra trung
        if (!Objects.equals(requestDto.getReceiptNumber(),exportReceipt.getReceiptNumber())
                && exportReceiptRepository.existsByReceiptNumber(requestDto.getReceiptNumber())) {
            throw new ConflictException(ErrorMessage.ExportReceipt.ERR_DUPLICATE_NUMBER, requestDto.getReceiptNumber());
        }
        //Cap nhat thong tin
        exportReceipt.setReceiptNumber(requestDto.getReceiptNumber());
        exportReceipt.setExportDate(requestDto.getExportDate());
        exportReceipt.setExportReason(requestDto.getExportReason());

        //Kiem tra va cap nhat danh sach sach
        Set<Long> newBookIds = requestDto.getBookIds();
        Set<Book> currentBooks = exportReceipt.getBooks();

        //Lay danh sach ID hien tai trong phieu xuat
        Set<Long> currentBookIds = currentBooks.stream()
                .map(Book::getId)
                .collect(Collectors.toSet());

        //Tim sach can them moi
        Set<Long> bookIdsToAdd = new HashSet<>(newBookIds);
        bookIdsToAdd.removeAll(currentBookIds);

        //Tim sach can xoa
        Set<Long> bookIdsToRemove = new HashSet<>();
        bookIdsToRemove.removeAll(newBookIds);

        //Them sach moi vao phieu xuat
        if (!bookIdsToAdd.isEmpty()) {
            for (Long bookId : bookIdsToAdd) {
                getBook(exportReceipt,bookId);
            }
        }

        //Xoa sach khong con trong danh sach
        if (!bookIdsToRemove.isEmpty()) {
            Set<Book> booksToRemove = currentBooks.stream()
                    .filter(book -> bookIdsToRemove.contains(book.getId()))
                    .collect(Collectors.toSet());

            for (Book book : booksToRemove) {
                book.setExportReceipt(null);
            }

            bookRepository.saveAll(booksToRemove);

            exportReceipt.getBooks().removeAll(booksToRemove);
        }

        exportReceiptRepository.save(exportReceipt);

        logService.createLog(TAG, EventConstants.EDIT, "Sửa phiếu xuất: " + exportReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {

        ExportReceipt exportReceipt = getEntity(id);

        // Xóa sách liên kết với phiếu xuất
        exportReceipt.getBooks().forEach(book -> {
            book.setExportReceipt(null);
            bookRepository.save(book);
        });
        exportReceipt.getBooks().clear();

        exportReceiptRepository.delete(exportReceipt);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa phiếu xuất: " + exportReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<ExportReceiptResponseDto> findAll(PaginationFullRequestDto requestDto) {

        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.EXPORT_RECEIPT);

        Page<ExportReceipt> page = exportReceiptRepository.findAll(
                ExportReceiptSpecification.filterExportReceipts(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<ExportReceiptResponseDto> items = page.getContent().stream()
                .map(ExportReceiptResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.EXPORT_RECEIPT, page);

        PaginationResponseDto<ExportReceiptResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public ExportReceiptResponseDto findById(Long id) {

        ExportReceipt exportReceipt = getEntity(id);
        ExportReceiptResponseDto responseDto = new ExportReceiptResponseDto(exportReceipt);

        for (Book book : exportReceipt.getBooks()) {
            responseDto.getBookIds().add(book.getId());
        }

        return responseDto;
    }
}
