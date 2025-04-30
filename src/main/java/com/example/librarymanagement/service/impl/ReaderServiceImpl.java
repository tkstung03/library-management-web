package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.CommonConstant;
import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.reader.CreateReaderCardRequestDto;
import com.example.librarymanagement.domain.dto.request.reader.ReaderRequestDto;
import com.example.librarymanagement.domain.dto.response.reader.ReaderDetailResponseDto;
import com.example.librarymanagement.domain.dto.response.reader.ReaderResponseDto;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.mapper.ReaderMapper;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ForbiddenException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.ReaderRepository;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.service.PdfService;
import com.example.librarymanagement.service.ReaderService;
import com.example.librarymanagement.service.SystemSettingService;
import com.example.librarymanagement.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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

    }

    @Override
    public CommonResponseDto save(ReaderRequestDto requestDto, MultipartFile image, String userId) {
        return null;
    }

    @Override
    public CommonResponseDto update(Long id, ReaderRequestDto requestDto, MultipartFile image, String userId) {
        return null;
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        return null;
    }

    @Override
    public PaginationResponseDto<ReaderResponseDto> findAll(PaginationFullRequestDto requestDto) {
        return null;
    }

    @Override
    public ReaderResponseDto findById(Long id) {
        return null;
    }

    @Override
    public ReaderResponseDto findByCardNumber(String cardNumber) {
        return null;
    }

    @Override
    public byte[] generateReaderCards(CreateReaderCardRequestDto requestDto) {
        return new byte[0];
    }

    @Override
    public ReaderDetailResponseDto getReaderDetailsByCardNumber(String cardNumber) {
        return null;
    }
}
