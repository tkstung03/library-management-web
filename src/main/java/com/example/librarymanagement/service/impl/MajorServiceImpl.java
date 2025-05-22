package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.EventConstants;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.major.MajorRequestDto;
import com.example.librarymanagement.domain.dto.response.major.MajorResponseDto;
import com.example.librarymanagement.domain.entity.Major;
import com.example.librarymanagement.domain.mapper.MajorMapper;
import com.example.librarymanagement.domain.specification.MajorSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ConflictException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.MajorRepository;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.service.MajorService;
import com.example.librarymanagement.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private static final String TAG = "Quản lý ngành học";

    private final MajorRepository majorRepository;

    private final MessageSource messageSource;

    private final MajorMapper majorMapper;

    private final LogService logService;

    @Override
    public void init(String majorCsvPath) {
        log.info("Initializing major import from CSV: {}", majorCsvPath);

        if (majorRepository.count() > 0) {
            log.info("Majors already existed in the database. Skipping import.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(majorCsvPath))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 1) continue;

                Major major = new Major();
                major.setName(values[0]);  // Giả sử cột đầu là tên ngành học

                if (!majorRepository.existsByName(major.getName())) {
                    majorRepository.save(major);
                    log.info("Successfully saved major: {}", major.getName());
                }
            }

            log.info("Major import completed successfully.");

        } catch (IOException e) {
            log.error("Error while initializing majors from CSV: {}", e.getMessage(), e);
        }
    }

    @Override
    public CommonResponseDto save(MajorRequestDto requestDto, String userId) {
        if (majorRepository.existsByName(requestDto.getName())) {
            throw new ConflictException(ErrorMessage.Major.ERR_DUPLICATE_NAME);
        }

        Major major = majorMapper.toMajor(requestDto);
        major.setActiveFlag(true);
        majorRepository.save(major);

        logService.createLog(TAG, EventConstants.ADD, "Tạo ngành học mới: " + major.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new MajorResponseDto(major));
    }

    @Override
    public CommonResponseDto update(Long id, MajorRequestDto requestDto, String userId) {
        Major major = findById(id);

        if (!Objects.equals(major.getName(), requestDto.getName()) && majorRepository.existsByName(requestDto.getName())) {
            throw new ConflictException(ErrorMessage.Major.ERR_DUPLICATE_NAME);
        }

        major.setName(requestDto.getName());
        majorRepository.save(major);

        logService.createLog(TAG, EventConstants.EDIT, "Cập nhật ngành học ID: " + major.getId() + ", tên mới: " + major.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new MajorResponseDto(major));
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        Major major = findById(id);

        // Nếu có bảng liên kết cần check ví dụ:
        if (!major.getReaders().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Major.ERR_HAS_LINKED_READERS);
        }

        majorRepository.delete(major);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa ngành học: " + major.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<MajorResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.MAJOR);

        Page<Major> page = majorRepository.findAll(
                MajorSpecification.filterMajors(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()),
                pageable);

        List<MajorResponseDto> items = page.getContent().stream()
                .map(MajorResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.MAJOR, page);

        PaginationResponseDto<MajorResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public Major findById(Long id) {
        return majorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Major.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id, String userId) {
        Major major = findById(id);

        major.setActiveFlag(!major.getActiveFlag());

        majorRepository.save(major);

        logService.createLog(TAG, EventConstants.EDIT, "Thay đổi trạng thái ngành học: " + major.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, major.getActiveFlag());
    }
}
