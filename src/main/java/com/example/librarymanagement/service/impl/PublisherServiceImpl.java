package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.publisher.PublisherRequestDto;
import com.example.librarymanagement.domain.entity.Publisher;
import com.example.librarymanagement.domain.mapper.PublisherMapper;
import com.example.librarymanagement.domain.specification.PublisherSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ConflictException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.PublisherRepository;
import com.example.librarymanagement.service.PublisherService;
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
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    private final MessageSource messageSource;

    private final PublisherMapper publisherMapper;
    @Override
    public void init(String publishersCsvPath) {
        log.info("Initializing publisher import from CSV: {}", publishersCsvPath);

        if (publisherRepository.count() > 0) {
            log.info("Publishers already exist in the database. Skipping import.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(publishersCsvPath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 4) continue;

                Publisher publisher = new Publisher();
                publisher.setCode(values[0]);
                publisher.setName(values[1]);
                publisher.setAddress(values[2]);
                publisher.setCity(values[3]);

                if (!publisherRepository.existsByCode(publisher.getCode())) {
                    publisherRepository.save(publisher);
                    log.info("Successfully saved publisher: {} (Code: {})", publisher.getName(), publisher.getCode());
                }
            }

        } catch (IOException e) {
            log.error("Error while initializing publishers from CSV: {}", e.getMessage(), e);
        }
    }

    @Override
    public CommonResponseDto save(PublisherRequestDto requestDto) {
        if (publisherRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.Publisher.ERR_DUPLICATE_CODE);
        }

        Publisher publisher = publisherMapper.toPublisher(requestDto);

        publisher.setActiveFlag(true);
        publisherRepository.save(publisher);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, publisher);
    }

    @Override
    public CommonResponseDto update(Long id, PublisherRequestDto requestDto) {
        Publisher publisher = findById(id);

        if (!Objects.equals(publisher.getCode(), requestDto.getCode()) && publisherRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.Publisher.ERR_DUPLICATE_CODE);
        }

        publisher.setCode(requestDto.getCode());
        publisher.setName(requestDto.getName());
        publisher.setAddress(requestDto.getAddress());
        publisher.setCity(requestDto.getCity());
        publisher.setNotes(requestDto.getNotes());

        publisherRepository.save(publisher);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, publisher);
    }

    @Override
    public CommonResponseDto delete(Long id) {
        Publisher publisher = findById(id);

        if (!publisher.getBookDefinitions().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Publisher.ERR_HAS_LINKED_BOOKS);
        }

        publisherRepository.delete(publisher);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<Publisher> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.PUBLISHER);

        Page<Publisher> page = publisherRepository.findAll(
                PublisherSpecification.filterPublishers(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()),
                pageable);

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.PUBLISHER, page);

        PaginationResponseDto<Publisher> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public Publisher findById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Publisher.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id) {
        Publisher publisher = findById(id);

        publisher.setActiveFlag(!publisher.getActiveFlag());

        publisherRepository.save(publisher);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, publisher.getActiveFlag());
    }
}
