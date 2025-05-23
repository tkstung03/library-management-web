package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.CardStatus;
import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.EventConstants;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.reader.ReaderViolationRequestDto;
import com.example.librarymanagement.domain.dto.response.reader.ReaderViolationResponseDto;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.entity.ReaderViolation;
import com.example.librarymanagement.domain.mapper.ReaderViolationMapper;
import com.example.librarymanagement.domain.specification.ReaderViolationSpecification;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.ReaderRepository;
import com.example.librarymanagement.repository.ReaderViolationRepository;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.service.ReaderViolationService;
import com.example.librarymanagement.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReaderViolationServiceImpl implements ReaderViolationService {

    private static final String TAG = "Quản lý vi phạm của bạn đọc";

    private final ReaderViolationRepository readerViolationRepository;

    private final ReaderViolationMapper readerViolationMapper;

    private final LogService logService;

    private final ReaderRepository readerRepository;

    private ReaderViolation getEntity(Long id) {
        return readerViolationRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.ReaderViolation.ERR_NOT_FOUND_ID, id));
    }

    private void getReader(ReaderViolationRequestDto requestDto, ReaderViolation readerViolation) {
        Reader reader = readerRepository.findById(requestDto.getReaderId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_ID, readerViolation.getId()));
        readerViolation.setReader(reader);

        switch (readerViolation.getPunishmentForm()) {
            case CARD_REVOCATION -> reader.setStatus(CardStatus.REVOKED);
            case CARD_SUSPENSION -> reader.setStatus(CardStatus.SUSPENDED );
        }
    }

    @Override
    public CommonResponseDto save(ReaderViolationRequestDto requestDto, String userId) {
        ReaderViolation violation = readerViolationMapper.toReaderViolation(requestDto);
        getReader(requestDto, violation);

        readerViolationRepository.save(violation);

        logService.createLog(TAG, EventConstants.ADD, "Tạo vi phạm mới cho bạn đọc: " + violation.getViolationDetails(), userId);

        return new CommonResponseDto("Vi phạm đã được thêm thành công.", new ReaderViolationResponseDto(violation));
    }

    @Override
    public CommonResponseDto update(Long id, ReaderViolationRequestDto requestDto, String userId) {
        ReaderViolation violation = readerViolationMapper.toReaderViolation(requestDto);
        if (violation.getReader() == null || !Objects.equals(violation.getReader().getId(), requestDto.getReaderId())) {
            getReader(requestDto, violation);
        }

        violation.setViolationDetails(requestDto.getViolationDetails());
        violation.setPunishmentForm(requestDto.getPunishmentForm());
        violation.setOtherPunishmentForm(requestDto.getOtherPenaltyForm());
        violation.setPenaltyDate(requestDto.getPenaltyDate());
        violation.setEndDate(requestDto.getEndDate());
        violation.setFineAmount(requestDto.getFineAmount());
        violation.setNotes(requestDto.getNotes());

        readerViolationRepository.save(violation);

        logService.createLog(TAG, EventConstants.EDIT, "Cập nhật vi phạm ID: " + violation.getId(), userId);

        return new CommonResponseDto("Vi phạm đã được cập nhật thành công.", new ReaderViolationResponseDto(violation));
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        ReaderViolation violation = getEntity(id);

        readerViolationRepository.delete(violation);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa vi phạm id: " + violation.getId(), userId);

        return new CommonResponseDto("Vi phạm đã được xóa thành công.");
    }

    @Override
    public PaginationResponseDto<ReaderViolationResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.READER_VIOLATION);

        Page<ReaderViolation> page = readerViolationRepository.findAll(
                ReaderViolationSpecification.filterReaderViolations(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<ReaderViolationResponseDto> items = page.getContent().stream()
                .map(ReaderViolationResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.READER_VIOLATION, page);

        PaginationResponseDto<ReaderViolationResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public ReaderViolationResponseDto findById(Long id) {
        ReaderViolation readerViolation = getEntity(id);
        return new ReaderViolationResponseDto(readerViolation);
    }
}
