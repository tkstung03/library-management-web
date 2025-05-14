package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.*;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.reader.CreateReaderCardRequestDto;
import com.example.librarymanagement.domain.dto.request.reader.ReaderRequestDto;
import com.example.librarymanagement.domain.dto.response.library.LibraryInfoResponseDto;
import com.example.librarymanagement.domain.dto.response.reader.ReaderDetailResponseDto;
import com.example.librarymanagement.domain.dto.response.reader.ReaderResponseDto;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.mapper.ReaderMapper;
import com.example.librarymanagement.domain.specification.ReaderSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ConflictException;
import com.example.librarymanagement.exception.ForbiddenException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.ReaderRepository;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.service.PdfService;
import com.example.librarymanagement.service.ReaderService;
import com.example.librarymanagement.service.SystemSettingService;
import com.example.librarymanagement.util.PaginationUtil;
import com.example.librarymanagement.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private static final String TAG = "Quản lý bạn đọc";

    private final ReaderRepository readerRepository;

    private final PasswordEncoder passwordEncoder;

    private final ReaderMapper readerMapper;

    private final LogService logService;

    private final UploadFileUtil uploadFileUtil;

    private final MessageSource messageSource;

    private final PdfService pdfService;

    private final SystemSettingService systemSettingService;

    public static void validateReaderStatus(Reader reader) {
        switch (reader.getStatus()) {
            case INACTIVE -> throw new ForbiddenException(ErrorMessage.Reader.ERR_READER_INACTIVE);
            case SUSPENDED -> throw new ForbiddenException(ErrorMessage.Reader.ERR_READER_SUSPENDED);
            case REVOKED -> throw new ForbiddenException(ErrorMessage.Reader.ERR_READER_REVOKED);
        }
        if (reader.getExpiryDate().isBefore(LocalDate.now())){
            throw new ForbiddenException(ErrorMessage.Reader.ERR_READER_EXPIRED);
        }
    }

    private Reader getEntity(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_ID, id));
    }

    private Reader getEntity(String cardNumber) {
        return readerRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER, cardNumber));
    }

    public void validatePassword(String password) {
        if (!password.matches(CommonConstant.REGEXP_PASSWORD)) {
            throw new BadRequestException(ErrorMessage.INVALID_FORMAT_PASSWORD);
        }
    }
    @Override
    public void init(String readerCsvPath) {
        log.info("Initializing reader import from CSV: {}", readerCsvPath);

        if (readerRepository.count() > 0) {
            log.info("Readers already exist in the database. Skipping import.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(readerCsvPath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 3) continue;

                Reader reader = new Reader();
                reader.setCardType(CardType.STUDENT);
                reader.setFullName(values[0]);
                reader.setEmail(values[1]);
                reader.setCardNumber(values[2]);
                reader.setPassword(passwordEncoder.encode(values[3]));
                reader.setCreatedDate(LocalDate.now());
                reader.setExpiryDate(LocalDate.now().plusMonths(1));
                reader.setGender(Gender.OTHER);
                reader.setStatus(CardStatus.ACTIVE);

                if (!readerRepository.existsByCardNumber(reader.getCardNumber())
                        && !readerRepository.existsByEmail(reader.getEmail())) {
                    readerRepository.save(reader);
                    log.info("Successfully saved reader: {}", reader.getFullName());
                }
            }

            log.info("Reader import completed successfully.");
        } catch (IOException e) {
            log.error("Error while saving reader: {}", e.getMessage(), e);
        }
    }

    @Override
    public CommonResponseDto save(ReaderRequestDto requestDto, MultipartFile image, String userId) {

        //Kiểm tra mật khẩu
        String password = requestDto.getPassword();
        if (password == null || password.isEmpty()) {
            throw new BadRequestException(ErrorMessage.INVALID_NOT_BLANK_FIELD);
        } else {
            validatePassword(password);
        }

        //Kiểm tra file tải lên có phải định dạng ảnh không
        uploadFileUtil.checkImageIsValid(image);

        if (readerRepository.existsByCardNumber(requestDto.getCardNumber())) {
            throw new ConflictException(ErrorMessage.Reader.ERR_DUPLICATE_CARD_NUMBER, requestDto.getCardNumber());
        }

        if (readerRepository.existsByEmail(requestDto.getEmail())) {
            throw new ConflictException(ErrorMessage.Reader.ERR_DUPLICATE_EMAIL, requestDto.getEmail());
        }

        Reader reader = readerMapper.toReader(requestDto);
        reader.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        reader.setCreatedDate(LocalDate.now());
        if (image != null && !image.isEmpty()) {
            String newImageUrl = uploadFileUtil.uploadFile(image);
            reader.setAvatar(newImageUrl);
        }

        readerRepository.save(reader);

        logService.createLog(TAG, EventConstants.ADD, "Thêm thẻ bạn đọc mới: " + reader.getCardNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new ReaderResponseDto(reader));
    }

    @Override
    public CommonResponseDto update(Long id, ReaderRequestDto requestDto, MultipartFile image, String userId) {
        //Kiểm tra file tải lên có phải định dạng ảnh không
        uploadFileUtil.checkImageIsValid(image);

        Reader reader = getEntity(id);

        //Nếu mật khẩu khác null thì cập nhật lại mật khẩu
        String password = requestDto.getPassword();
        if (password != null && !password.isEmpty()) {
            validatePassword(password);
            reader.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        if (!Objects.equals(reader.getCardNumber(), requestDto.getCardNumber()) &&
                readerRepository.existsByCardNumber(requestDto.getCardNumber())) {
            throw new ConflictException(ErrorMessage.Reader.ERR_DUPLICATE_CARD_NUMBER, requestDto.getCardNumber());
        }

        if (!Objects.equals(reader.getEmail(), requestDto.getEmail()) &&
                readerRepository.existsByEmail(requestDto.getEmail())) {
            throw new ConflictException(ErrorMessage.Reader.ERR_DUPLICATE_EMAIL, requestDto.getEmail());
        }

        if (image != null && !image.isEmpty()) {
            String newImageUrl = uploadFileUtil.uploadFile(image);

            uploadFileUtil.destroyFileWithUrl(reader.getAvatar());

            reader.setAvatar(newImageUrl);
        }

        reader.setCardType(requestDto.getCardType());
        reader.setFullName(requestDto.getFullName());
        reader.setDateOfBirth(requestDto.getDateOfBirth());
        reader.setGender(requestDto.getGender());
        reader.setAddress(requestDto.getAddress());
        reader.setEmail(requestDto.getEmail());
        reader.setPhoneNumber(requestDto.getPhoneNumber());
        reader.setCardNumber(requestDto.getCardNumber());
        reader.setExpiryDate(requestDto.getExpiryDate());
        reader.setStatus(requestDto.getStatus());

        readerRepository.save(reader);

        logService.createLog(TAG, EventConstants.EDIT, "Sửa thẻ bạn đọc: " + reader.getCardNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new ReaderResponseDto(reader));
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {

        Reader reader = getEntity(id);

        if (!reader.getBorrowReceipts().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Reader.ERR_READER_HAS_DATA);
        }

        if (!reader.getLibraryVisits().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Reader.ERR_READER_HAS_DATA);
        }

        uploadFileUtil.destroyFileWithUrl(reader.getAvatar());

        readerRepository.delete(reader);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa thẻ bạn đọc: " + reader.getCardNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<ReaderResponseDto> findAll(PaginationFullRequestDto requestDto) {

        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.READER);

        Page<Reader> page = readerRepository.findAll(
                ReaderSpecification.filterReaders(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<ReaderResponseDto> items = page.getContent().stream()
                .map(ReaderResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.READER, page);

        PaginationResponseDto<ReaderResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public ReaderResponseDto findById(Long id) {

        Reader reader = getEntity(id);
        return new ReaderResponseDto(reader);
    }

    @Override
    public ReaderResponseDto findByCardNumber(String cardNumber) {

        Reader reader = getEntity(cardNumber);
        return new ReaderResponseDto(reader);
    }

    @Override
    public byte[] generateReaderCards(CreateReaderCardRequestDto requestDto) {

        List<Reader> readers = readerRepository.findAllByIdIn(requestDto.getReaderIds());
        if (readers.isEmpty()) {
            throw new BadRequestException(ErrorMessage.Reader.ERR_NOT_FOUND_ID, requestDto.getReaderIds());
        }
        LibraryInfoResponseDto libraryInfo = systemSettingService.getLibraryInfo();
        return pdfService.createReaderCard(libraryInfo.getEducationOffice(), libraryInfo.getSchool(), libraryInfo.getPrincipalName(), readers);
    }

    @Override
    public ReaderDetailResponseDto getReaderDetailsByCardNumber(String cardNumber) {

        Reader reader = readerRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER, cardNumber));

        return new ReaderDetailResponseDto(reader);
    }
}
