package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.EventConstants;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.filter.BookDefinitionFilter;
import com.example.librarymanagement.domain.dto.filter.QueryFilter;
import com.example.librarymanagement.domain.dto.pagination.*;
import com.example.librarymanagement.domain.dto.request.book.BookDefinitionRequestDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookByBookDefinitionResponseDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookDefinitionResponseDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookDetailForReaderResponseDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookForReaderResponseDto;
import com.example.librarymanagement.domain.entity.*;
import com.example.librarymanagement.domain.mapper.BookDefinitionMapper;
import com.example.librarymanagement.domain.specification.BookDefinitionSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.*;
import com.example.librarymanagement.service.BookDefinitionService;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.service.PdfService;
import com.example.librarymanagement.service.SystemSettingService;
import com.example.librarymanagement.util.PaginationUtil;
import com.example.librarymanagement.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.librarymanagement.domain.specification.BookDefinitionSpecification.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookDefinitionServiceImpl implements BookDefinitionService {

    private static final String TAG = "Quản lý biên mục";

    private final UploadFileUtil uploadFileUtil;

    private final BookDefinitionRepository bookDefinitionRepository;

    private final BookDefinitionMapper bookDefinitionMapper;

    private final MessageSource messageSource;

    private final CategoryRepository categoryRepository;

    private final BookSetRepository bookSetRepository;

    private final AuthorRepository authorRepository;

    private final PublisherRepository publisherRepository;

    private final BookAuthorRepository bookAuthorRepository;

    private final ClassificationSymbolRepository classificationSymbolRepository;

    private final BookRepository bookRepository;

    private final LogService logService;

    private final PdfService pdfService;

    private final SystemSettingService systemSettingService;

    @Override
    public void init(String bookDefinitionCsvPath) {
        log.info("Initializing book definition import from CSV: {}", bookDefinitionCsvPath);

        if (bookDefinitionRepository.count() > 0) {
            log.info("Book definitions already exist in the database. Skipping import.");
            return;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(bookDefinitionCsvPath))){
            String line;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 3) continue;

                BookDefinition bookDefinition = new BookDefinition();
                bookDefinition.setTitle(values[0]);
                bookDefinition.setBookNumber(values[1]);

                Long categoryId;
                try {
                    categoryId = Long.parseLong(values[2].trim());
                } catch (NumberFormatException e) {
                    log.warn("Skipping book '{}' due to invalid category ID '{}'. Error: {}", values[0], values[2], e.getMessage());
                    continue;
                }

                try {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new IllegalArgumentException("Category not found for ID: " + categoryId));
                    bookDefinition.setCategory(category);
                } catch (IllegalArgumentException e) {
                    log.warn("Skipping book '{}' because category ID '{}' was not found.", values[0], categoryId);
                    continue;
                }

                if (!bookDefinitionRepository.existsByBookNumber(bookDefinition.getBookNumber())) {
                    bookDefinitionRepository.save(bookDefinition);
                    log.info("Successfully saved book definition: '{}' (Book number: {}, Category: {}",
                            bookDefinition.getTitle(), bookDefinition.getBookNumber(), bookDefinition.getCategory().getCategoryName());
                }
            }

            log.info("Book definition import completed successfully!" );
        }
        catch (IOException e) {
            log.error("Error while initializing book definitions from CSV: {}", e.getMessage(), e);
        }
    }

    @Override
    public CommonResponseDto save(BookDefinitionRequestDto requestDto, MultipartFile file, MultipartFile pdf, String userId) {
        uploadFileUtil.checkImageIsValid(file);

        if (bookDefinitionRepository.existsByBookNumber(requestDto.getBookNumber())){
            throw new BadRequestException(ErrorMessage.BookDefinition.ERR_DUPLICATE_CODE, requestDto.getBookNumber());
        }

        BookDefinition bookDefinition = bookDefinitionMapper.toBookDefinition(requestDto);
        bookDefinition.setBookAuthors(new ArrayList<>());

        //Lưu danh mục
        Category category = categoryRepository.findByIdAndActiveFlagIsTrue(requestDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId()));
        bookDefinition.setCategory(category);

        //Lưu bộ sách
        if (requestDto.getBookSetId() != null) {
            Publisher publisher = publisherRepository.findByIdAndActiveFlagIsTrue(requestDto.getPublisherId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Publisher.ERR_NOT_FOUND_ID, requestDto.getPublisherId()));
        }

        //Lưu nhà xuất bản
        if (requestDto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findByIdAndActiveFlagIsTrue(requestDto.getPublisherId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Publisher.ERR_NOT_FOUND_ID, requestDto.getPublisherId()));
            bookDefinition.setPublisher(publisher);
        }

        //Lưu danh mục phân loại
        if (requestDto.getClassificationSymbolId() != null) {
            ClassificationSymbol classificationSymbol = classificationSymbolRepository.findByIdAndActiveFlagIsTrue(requestDto.getClassificationSymbolId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassificationSymbol.ERR_NOT_FOUND_ID, requestDto.getClassificationSymbolId()));
            bookDefinition.setClassificationSymbol(classificationSymbol);
        }

        //Lưu ds tác giả
        if (requestDto.getAuthorIds() != null) {
            requestDto.getAuthorIds().forEach(authorId -> {
                Author author = authorRepository.findByIdAndActiveFlagIsTrue(authorId)
                        .orElseThrow(() -> new NotFoundException(ErrorMessage.Author.ERR_NOT_FOUND_ID, authorId));

                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setAuthor(author);
                bookAuthor.setBookDefinition(bookDefinition);

                bookDefinition.getBookAuthors().add(bookAuthor);
            } );
        }

        // Xử lý upload ảnh, ưu tiên file upload, rồi đến image url
        if (file != null && !file.isEmpty()) {
            String newImageUrl = uploadFileUtil.uploadFile(file);
            bookDefinition.setImageUrl(newImageUrl);
        } else if (requestDto.getImageUrl() != null) {
            String newImageUrl = uploadFileUtil.copyImageFromUrl(requestDto.getImageUrl());
            bookDefinition.setImageUrl(newImageUrl);
        }

        // === Lưu file PDF vào local ===
        if (pdf != null && !pdf.isEmpty()) {
            try {
                // Lấy đường dẫn thư mục gốc của dự án
                String projectDir = System.getProperty("user.dir");

                // Tạo đường dẫn đầy đủ tới thư mục uploads/pdfs
                Path uploadPath = Paths.get(projectDir, "uploads", "pdfs");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath); // Tạo thư mục nếu chưa tồn tại
                }

                // Tạo tên file duy nhất
                String fileName = UUID.randomUUID() + "_" + pdf.getOriginalFilename();

                // Đường dẫn lưu file
                Path filePath = uploadPath.resolve(fileName);

                // Lưu file vào đĩa
                pdf.transferTo(filePath.toFile());

                // Gán đường dẫn vào entity (dùng đường dẫn tương đối để phục vụ truy cập sau này)
                bookDefinition.setPdfUrl("/uploads/pdfs/" + fileName);

            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi lưu file PDF: " + e.getMessage(), e);
            }
        }


        bookDefinition.setActiveFlag(true);
        bookDefinitionRepository.save(bookDefinition);

        logService.createLog(TAG, EventConstants.ADD, "Thêm biên mục mới: " + bookDefinition.getTitle(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto update(Long id, BookDefinitionRequestDto requestDto, MultipartFile file, MultipartFile pdf, String userId) {
        uploadFileUtil.checkImageIsValid(file);

        BookDefinition bookDefinition = bookDefinitionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, id));

        //Kiểm tra kí hiệu tên sách
        if (!Objects.equals(bookDefinition.getBookNumber(), requestDto.getBookNumber()) && bookDefinitionRepository.existsByBookNumber(requestDto.getBookNumber())) {
            throw new BadRequestException(ErrorMessage.BookDefinition.ERR_DUPLICATE_CODE, requestDto.getBookNumber());
        }

        //Cap nhat danh muc
        Category category = categoryRepository.findByIdAndActiveFlagIsTrue(requestDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId()));
        bookDefinition.setCategory(category);

        // Cập nhật bộ sách
        if (requestDto.getBookSetId() != null) {
            BookSet bookSet = bookSetRepository.findByIdAndActiveFlagIsTrue(requestDto.getBookSetId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.BookSet.ERR_NOT_FOUND_ID, requestDto.getBookSetId()));
            bookDefinition.setBookSet(bookSet);
        } else {
            bookDefinition.setBookSet(null);
        }

        // Cập nhật nhà xuất bản
        if (requestDto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findByIdAndActiveFlagIsTrue(requestDto.getPublisherId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Publisher.ERR_NOT_FOUND_ID, requestDto.getPublisherId()));
            bookDefinition.setPublisher(publisher);
        } else {
            bookDefinition.setPublisher(null);
        }

        // Cập nhật biểu tượng phân loại
        if (requestDto.getClassificationSymbolId() != null) {
            ClassificationSymbol classificationSymbol = classificationSymbolRepository.findByIdAndActiveFlagIsTrue(requestDto.getClassificationSymbolId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassificationSymbol.ERR_NOT_FOUND_ID, requestDto.getClassificationSymbolId()));
            bookDefinition.setClassificationSymbol(classificationSymbol);
        } else {
            bookDefinition.setClassificationSymbol(null);
        }

        // Cập nhật danh sách tác giả
        Set<Long> newAuthorIds = requestDto.getAuthorIds(); // Mảng tác giả mới

        // Xóa các tác giả không còn trong danh sách mới
        if (newAuthorIds == null || newAuthorIds.isEmpty()) {
            bookAuthorRepository.deleteAllByBookDefinitionId(bookDefinition.getId()); // Xóa các bản ghi trong cơ sở dữ liệu
            bookDefinition.getBookAuthors().clear(); // Xóa các bản ghi trong bộ nhớ
        } else {
            // Tập hợp ID tác giả hiện tại
            Set<Long> currentAuthorIds = bookDefinition.getBookAuthors().stream()
                    .map(bookAuthor -> bookAuthor.getAuthor().getId())
                    .collect(Collectors.toSet());

            // Xóa các tác giả không có trong danh sách mới
            for (Long currentAuthorId : currentAuthorIds) {
                if (!newAuthorIds.contains(currentAuthorId)) {
                    // Tìm BookAuthor để xóa
                    BookAuthor bookAuthorToRemove = bookDefinition.getBookAuthors().stream()
                            .filter(bookAuthor -> bookAuthor.getAuthor().getId().equals(currentAuthorId))
                            .findFirst()
                            .orElse(null);
                    if (bookAuthorToRemove != null) {
                        bookDefinition.getBookAuthors().remove(bookAuthorToRemove);
                        bookAuthorRepository.delete(bookAuthorToRemove); // Xóa trong cơ sở dữ liệu
                    }
                }
            }

            // Thêm các tác giả mới
            for (Long authorId : newAuthorIds) {
                if (!currentAuthorIds.contains(authorId)) { // Nếu tác giả chưa tồn tại
                    Author author = authorRepository.findByIdAndActiveFlagIsTrue(authorId)
                            .orElseThrow(() -> new NotFoundException(ErrorMessage.Author.ERR_NOT_FOUND_ID, authorId));

                    BookAuthor bookAuthor = new BookAuthor();
                    bookAuthor.setAuthor(author);
                    bookAuthor.setBookDefinition(bookDefinition);
                    bookDefinition.getBookAuthors().add(bookAuthor);
                }
            }
        }

        // Cập nhật các thông tin khác
        bookDefinition.setTitle(requestDto.getTitle());
        bookDefinition.setPublishingYear(requestDto.getPublishingYear());
        bookDefinition.setPrice(requestDto.getPrice());
        bookDefinition.setEdition(requestDto.getEdition());
        bookDefinition.setReferencePrice(requestDto.getReferencePrice());
        bookDefinition.setPublicationPlace(requestDto.getPublicationPlace());
        bookDefinition.setBookNumber(requestDto.getBookNumber());
        bookDefinition.setPageCount(requestDto.getPageCount());
        bookDefinition.setBookSize(requestDto.getBookSize());
        bookDefinition.setParallelTitle(requestDto.getParallelTitle());
        bookDefinition.setSummary(requestDto.getSummary());
        bookDefinition.setSubtitle(requestDto.getSubtitle());
        bookDefinition.setAdditionalMaterial(requestDto.getAdditionalMaterial());
        bookDefinition.setKeywords(requestDto.getKeywords());
        bookDefinition.setIsbn(requestDto.getIsbn());
        bookDefinition.setLanguage(requestDto.getLanguage());
        bookDefinition.setSeries(requestDto.getSeries());
        bookDefinition.setAdditionalInfo(requestDto.getAdditionalInfo());

        // Xử lý upload ảnh, ưu tiên file upload, rồi đến image url
        if (file != null && !file.isEmpty()) {
            String newImageUrl = uploadFileUtil.uploadFile(file);

            //Xóa ảnh cũ
            uploadFileUtil.destroyFileWithUrl(bookDefinition.getImageUrl());

            bookDefinition.setImageUrl(newImageUrl);
        }

        // === Cập nhật file PDF nếu có ===
        if (pdf != null && !pdf.isEmpty()) {
            try {
                // Lấy đường dẫn thư mục gốc của dự án
                String projectDir = System.getProperty("user.dir");

                // Tạo đường dẫn đầy đủ tới thư mục uploads/pdfs
                Path uploadPath = Paths.get(projectDir, "uploads", "pdfs");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Tạo tên file mới duy nhất
                String fileName = UUID.randomUUID() + "_" + pdf.getOriginalFilename();

                // Đường dẫn lưu file
                Path filePath = uploadPath.resolve(fileName);

                // Lưu file PDF mới
                pdf.transferTo(filePath.toFile());

                // Xóa file PDF cũ nếu có (lấy đường dẫn tương đối lưu trong pdfUrl)
                if (bookDefinition.getPdfUrl() != null) {
                    // Giải đường dẫn cũ thành đường dẫn thực tế trên disk
                    Path oldFilePath = Paths.get(projectDir, bookDefinition.getPdfUrl().replaceFirst("^/", ""));
                    UploadFileUtil.deleteLocalFile(oldFilePath.toString());
                }

                // Cập nhật đường dẫn file PDF mới (relative path để truy cập qua web)
                bookDefinition.setPdfUrl("/uploads/pdfs/" + fileName);

            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi lưu file PDF: " + e.getMessage(), e);
            }
        }


        // Lưu đối tượng bookDefinition đã cập nhật
        bookDefinitionRepository.save(bookDefinition);

        //Ghi log
        logService.createLog(TAG, EventConstants.EDIT, "Cập nhật biên mục id: " + bookDefinition.getId(), userId);

        // Trả về kết quả sau khi cập nhật thành công
        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        BookDefinition bookDefinition = findEntityById(id);

        if (!bookDefinition.getBooks().isEmpty()) {
            throw new BadRequestException(ErrorMessage.BookDefinition.ERR_HAS_LINKED_BOOKS);
        }

        uploadFileUtil.destroyFileWithUrl(bookDefinition.getImageUrl());

        bookDefinitionRepository.delete(bookDefinition);

        logService.createLog(TAG,EventConstants.DELETE, "Xóa biên mục " + bookDefinition.getTitle(),userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    private BookDefinition findEntityById(Long id) {
        return bookDefinitionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, id));
    }
    @Override
    public PaginationResponseDto<BookDefinitionResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_DEFINITION);

        Specification<BookDefinition> spec = Specification.where(baseFilterBookDefinitions(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()));

        Page<BookDefinition> page = bookDefinitionRepository.findAll(spec, pageable);
        List<BookDefinitionResponseDto> items = page.getContent().stream()
                .map(BookDefinitionResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto,SortByDataConstant.BOOK_DEFINITION, page);

        PaginationResponseDto<BookDefinitionResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);
        return responseDto;
    }

    @Override
    public List<BookDefinitionResponseDto> findByIds(Set<Long> ids) {
        return bookDefinitionRepository.findBookDefinitionsByIds(ids);
    }

    @Override
    public BookDefinitionResponseDto findById(Long id) {
        BookDefinition bookDefinition = findEntityById(id);
        return new BookDefinitionResponseDto(bookDefinition);
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id, String userId) {
        BookDefinition bookDefinition = findEntityById(id);

        bookDefinition.setActiveFlag(!bookDefinition.getActiveFlag());

        bookDefinitionRepository.save(bookDefinition);

        logService.createLog(TAG,EventConstants.EDIT,"Thay đổi trạng thái biên mục: " + bookDefinition.getActiveFlag(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, bookDefinition.getActiveFlag());
    }

    @Override
    public PaginationResponseDto<BookByBookDefinitionResponseDto> getBooks(PaginationFullRequestDto requestDto, Long categoryGroupId, Long categoryId) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_DEFINITION);

        Specification<BookDefinition> spec = Specification.where(baseFilterBookDefinitions(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()))
                .and(filterByCategoryId(categoryId))
                .and(filterByCategoryGroupId(categoryGroupId));

        Page<BookDefinition> page = bookDefinitionRepository.findAll(spec, pageable);

        List<BookByBookDefinitionResponseDto> items = page.getContent().stream()
                .map(BookByBookDefinitionResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK_DEFINITION, page);

        PaginationResponseDto<BookByBookDefinitionResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public PaginationResponseDto<BookForReaderResponseDto> getBooksForUser(PaginationFullRequestDto requestDto, Long categoryGroupId, Long categoryId, Long authorId, String filterType) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_DEFINITION);

        Specification<BookDefinition> spec = Specification.where(baseFilterBookDefinitions(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()))
                .and(filterByCategoryId(categoryId))
                .and(filterByCategoryGroupId(categoryGroupId))
                .and(filterByAuthorId(authorId))
                .and(filterByBooksCountGreaterThanZero());

        if ("most_borrowed".equalsIgnoreCase(filterType)) {
            spec = spec.and(orderByBorrowCount());
        } else if ("new_releases".equalsIgnoreCase(filterType)) {
            spec = spec.and(orderByNewReleases());
        }

        Page<BookDefinition> page = bookDefinitionRepository.findAll(spec, pageable);

        List<BookForReaderResponseDto> items = page.getContent().stream()
                .map(BookForReaderResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK_DEFINITION, page);

        PaginationResponseDto<BookForReaderResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public BookDetailForReaderResponseDto getBookDetailForUser(Long id) {
        BookDefinition bookDefinition = findEntityById(id);
        if (bookDefinition.getBooks().isEmpty()) {
            throw new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, id);
        }

        return new BookDetailForReaderResponseDto(bookDefinition);
    }

    @Override
    public PaginationResponseDto<BookForReaderResponseDto> advancedSearchBooks(List<QueryFilter> queryFilters, PaginationSortRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_DEFINITION);

        Specification<BookDefinition> spec = filterByBooksCountGreaterThanZero()
                .and(BookDefinitionSpecification.getSpecificationFromFilters(queryFilters));

        Page<BookDefinition> page = bookDefinitionRepository.findAll(spec, pageable);

        List<BookForReaderResponseDto> items = page.getContent().stream()
                .map(BookForReaderResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK_DEFINITION, page);

        PaginationResponseDto<BookForReaderResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public PaginationResponseDto<BookForReaderResponseDto> searchBooks(BookDefinitionFilter filters, PaginationSortRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_DEFINITION);

        Specification<BookDefinition> spec = filterByBooksCountGreaterThanZero()
                .and(BookDefinitionSpecification.filterBookDefinitions(filters));

        Page<BookDefinition> page = bookDefinitionRepository.findAll(spec, pageable);

        List<BookForReaderResponseDto> items = page.getContent().stream()
                .map(BookForReaderResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK_DEFINITION, page);

        PaginationResponseDto<BookForReaderResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public byte[] getBooksPdfContent(Set<Long> ids) {
        List<Book> books = bookRepository.findAllByBookDefinitionIdIn(ids);
        return pdfService.createPdfFromBooks(books);
    }

    @Override
    public byte[] getBooksLabelType1PdfContent(Set<Long> ids) {
        List<Book> books = bookRepository.findAllByBookDefinitionIdIn(ids);
        return pdfService.createLabelType1Pdf(systemSettingService.getLibraryInfo().getLibrarySymbol(), books);
    }

    @Override
    public byte[] getBooksLabelType2PdfContent(Set<Long> ids) {
        List<Book> books = bookRepository.findAllByBookDefinitionIdIn(ids);
        return pdfService.createLabelType2Pdf(books);
    }

    @Override
    public byte[] generateBookListPdf() {
        List<Book> books = bookRepository.findAll();
        return pdfService.createBookListPdf(books);
    }
}
