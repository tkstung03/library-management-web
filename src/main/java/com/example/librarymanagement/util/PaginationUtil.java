package com.example.librarymanagement.util;

import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.domain.dto.pagination.PaginationRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationSortRequestDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


public class PaginationUtil {

    public static Pageable buildPageable(PaginationRequestDto request) {
        return PageRequest.of(request.getPageNumb(), request.getPageSize());
    }

    public static Pageable buildPageable(PaginationSortRequestDto requestDto, SortByDataConstant constant) {
        Sort sort = Sort.by(requestDto.getSortBy(constant));
        sort = requestDto.getIsAscending() ? sort.ascending() : sort.descending();
        return PageRequest.of(requestDto.getPageNumb(), requestDto.getPageSize(), sort);
    }

    public static Pageable buildPageable(PaginationRequestDto requestDto, List<String> sortByFields, List<Boolean> sortDirections) {
        if (sortByFields.size() != sortDirections.size()){
            throw new IllegalArgumentException("Number of sort fields and sort directions must match");
        }
        Sort sort = Sort.by(sortByFields.stream()
                .map(field -> sortDirections.get(sortByFields.indexOf(field)) ? Sort.Order.asc(field) : Sort.Order.desc(field))
                .toArray(Sort.Order[] :: new)
        );

        return PageRequest.of(requestDto.getPageNumb(), requestDto.getPageSize(), sort);
    }
}
