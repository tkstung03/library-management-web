package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.other.ClassificationSymbolRequestDto;
import com.example.librarymanagement.domain.entity.ClassificationSymbol;
import com.example.librarymanagement.domain.mapper.ClassificationSymbolMapper;
import com.example.librarymanagement.domain.specification.ClassificationSymbolSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ConflictException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.ClassificationSymbolRepository;
import com.example.librarymanagement.service.ClassificationSymbolService;
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
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClassificationSymbolServiceImpl implements ClassificationSymbolService {

    private final ClassificationSymbolRepository classificationSymbolRepository;

    private final ClassificationSymbolMapper classificationSymbolMapper;

    private final MessageSource messageSource;

    @Override
    public void init(String classificationSymbolCsvPath) {
        log.info("Initializing classification symbol import from CSV: {}", classificationSymbolCsvPath);

        if (classificationSymbolRepository.count() > 0) {
            log.info("Classification symbols already exist in the database. Skipping import.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(classificationSymbolCsvPath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 3) continue;

                ClassificationSymbol classificationSymbol = new ClassificationSymbol();
                classificationSymbol.setName(values[0]);
                classificationSymbol.setCode(values[1]);

                try {
                    int level = Integer.parseInt(values[2].trim());
                    classificationSymbol.setLevel(level);
                } catch (NumberFormatException e) {
                    log.warn("Skipping classification symbol '{}': Invalid level value '{}' (must be an integer). Error: {}",
                            values[1], values[2], e.getMessage());
                    continue;
                }

                if (!classificationSymbolRepository.existsByCode(classificationSymbol.getCode())) {
                    classificationSymbolRepository.save(classificationSymbol);
                    log.info("Successfully saved classification symbol: {} (Code: {}, Level: {})",
                            classificationSymbol.getName(), classificationSymbol.getCode(), classificationSymbol.getLevel());
                }
            }

            log.info("Classification symbol import completed successfully.");
        } catch (IOException e) {
            log.error("Error while initializing classification symbols from CSV: {}", e.getMessage(), e);
        }
    }

    @Override
    public CommonResponseDto save(ClassificationSymbolRequestDto requestDto) {
        if (classificationSymbolRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.ClassificationSymbol.ERR_DUPLICATE_CODE);
        }

        ClassificationSymbol classificationSymbol = classificationSymbolMapper.toClassificationSymbol(requestDto);

        classificationSymbol.setActiveFlag(true);
        classificationSymbolRepository.save(classificationSymbol);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, classificationSymbol);
    }

    @Override
    public CommonResponseDto update(Long id, ClassificationSymbolRequestDto requestDto) {
        ClassificationSymbol classificationSymbol = findById(id);

        if (!Objects.equals(classificationSymbol.getCode(), requestDto.getCode()) && classificationSymbolRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.ClassificationSymbol.ERR_DUPLICATE_CODE);
        }

        classificationSymbol.setName(requestDto.getName());
        classificationSymbol.setCode(requestDto.getCode());
        classificationSymbol.setLevel(requestDto.getLevel());

        classificationSymbolRepository.save(classificationSymbol);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, classificationSymbol);
    }

    @Override
    public CommonResponseDto delete(Long id) {
        ClassificationSymbol classificationSymbol = findById(id);

        if (!classificationSymbol.getBookDefinitions().isEmpty()) {
            throw new BadRequestException(ErrorMessage.ClassificationSymbol.ERR_HAS_LINKED_BOOKS);
        }

        classificationSymbolRepository.delete(classificationSymbol);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<ClassificationSymbol> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CLASSIFICATION_SYMBOL);

        Page<ClassificationSymbol> page = classificationSymbolRepository.findAll(
                ClassificationSymbolSpecification.filterClassificationSymbols(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()),
                pageable);

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CLASSIFICATION_SYMBOL, page);

        PaginationResponseDto<ClassificationSymbol> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public ClassificationSymbol findById(Long id) {
        return classificationSymbolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassificationSymbol.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id) {
        ClassificationSymbol classificationSymbol = findById(id);

        classificationSymbol.setActiveFlag(!classificationSymbol.getActiveFlag());

        classificationSymbolRepository.save(classificationSymbol);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, classificationSymbol.getActiveFlag());
    }
}
