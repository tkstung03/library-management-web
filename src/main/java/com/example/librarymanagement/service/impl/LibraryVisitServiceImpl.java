package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.filter.LibraryVisitFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.library.LibraryVisitRequestDto;
import com.example.librarymanagement.domain.dto.response.library.LibraryVisitResponseDto;
import com.example.librarymanagement.domain.entity.LibraryVisit;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.specification.LibraryVisitSpecification;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.LibraryVisitRepository;
import com.example.librarymanagement.repository.ReaderRepository;
import com.example.librarymanagement.service.LibraryVisitService;
import com.example.librarymanagement.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryVisitServiceImpl implements LibraryVisitService {

    private final LibraryVisitRepository libraryVisitRepository;

    private final ReaderRepository readerRepository;

    private final MessageSource messageSource;

    @Override
    public CommonResponseDto save(LibraryVisitRequestDto requestDto) {
        //Lấy ra bạn đọc
        Reader reader = readerRepository.findByCardNumber(requestDto.getCardNumber())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER, requestDto.getCardNumber()));

        ReaderServiceImpl.validateReaderStatus(reader);

        //Lấy ra thời gian bắt đầu vào kết thúc của hôm nay
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        //Tìm lần truy cập gần nhất trong ngày
        LibraryVisit lastVisited = libraryVisitRepository.findTopByReaderIdAndEntryTimeBetweenOrderByEntryTimeDesc(reader.getId(), startOfDay,endOfDay);

        if (lastVisited != null && lastVisited.getExitTime() == null) {
            // Nếu đã có lần truy cập trong ngày và chưa có thời gian thoát, cập nhật thời gian thoát
            lastVisited.setExitTime(LocalDateTime.now());
            libraryVisitRepository.save(lastVisited);

            String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message, new LibraryVisitResponseDto(lastVisited));
        } else {
            // Nếu đã có lượt truy cập và thời gian thoát, tạo mới
            LibraryVisit newVisit = new LibraryVisit();
            newVisit.setReader(reader);
            newVisit.setEntryTime(LocalDateTime.now());
            libraryVisitRepository.save(newVisit);

            String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message, new LibraryVisitResponseDto(newVisit));
        }
    }

    @Override
    public CommonResponseDto update(Long id, LibraryVisitRequestDto requestDto) {
        return null;
    }

    @Override
    public PaginationResponseDto<LibraryVisitResponseDto> findAll(PaginationFullRequestDto requestDto, LibraryVisitFilter filter) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.LIBRARY_VISIT);

        Page<LibraryVisit> page = libraryVisitRepository.findAll(
                LibraryVisitSpecification.filterLibraryVisits(requestDto.getKeyword(), requestDto.getSearchBy(), filter),
                pageable);

        List<LibraryVisitResponseDto> items = page.getContent().stream()
                .map(LibraryVisitResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.LIBRARY_VISIT, page);

        PaginationResponseDto<LibraryVisitResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public LibraryVisitResponseDto findById(Long id) {
        return null;
    }

    @Override
    public List<LibraryVisitResponseDto> getVisits(LocalDate entryTime, LocalDate exitTime, Long majorId) {
        LocalDateTime startDateTime = entryTime.atStartOfDay();
        LocalDateTime endDateTime = exitTime.atTime(23, 59, 59);

        List<LibraryVisit> visits;

        if (majorId != null) {
            visits = libraryVisitRepository.findByEntryTimeBetweenAndMajorId(startDateTime, endDateTime, majorId);
        } else {
            visits = libraryVisitRepository.findByEntryTimeBetween(startDateTime, endDateTime);
        }

        return visits.stream()
                .map(LibraryVisitResponseDto::new)
                .collect(Collectors.toList());
    }


    @Override
    public CommonResponseDto closeLibrary() {
        // Lấy thời gian đầu ngày và cuối ngày hôm nay
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        // Lấy tất cả các lần truy cập trong ngày hôm nay mà chưa có exitTime
        List<LibraryVisit> visitsToday = libraryVisitRepository.findAllByEntryTimeBetweenAndExitTimeIsNull(startOfDay,endOfDay);

        // Cập nhật exitTime cho tất cả các lần truy cập đó
        visitsToday.forEach(libraryVisit -> libraryVisit.setExitTime(LocalDateTime.now()));

        // Lưu lại các thay đổi
        libraryVisitRepository.saveAll(visitsToday);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }
}
