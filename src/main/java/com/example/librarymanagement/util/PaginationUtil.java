package com.example.librarymanagement.util;

import com.example.librarymanagement.constant.CommonConstant;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationSortRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import org.springframework.data.domain.Page;
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

    public static <T> PagingMeta buildPagingMeta(PaginationRequestDto request, Page<T> pages) {
        return buildPagingMeta(request.getPageNumb(), request.getPageSize(), null, null, pages);
    }

    public static <T> PagingMeta buildPagingMeta(PaginationSortRequestDto request, SortByDataConstant constant, Page<T> pages) {
        String sortBy = request.getSortBy(constant);
        String sortOrder = request.getIsAscending() ? CommonConstant.SORT_TYPE_ASC : CommonConstant.SORT_TYPE_DESC;

        return buildPagingMeta(request.getPageNumb(), request.getPageSize(), sortBy, sortOrder, pages);
    }

    public static <T> PagingMeta buildPagingMeta(PaginationFullRequestDto request, SortByDataConstant constant, Page<T> pages) {
        String sortBy = request.getSortBy(constant);
        String sortOrder = request.getIsAscending() ? CommonConstant.SORT_TYPE_ASC : CommonConstant.SORT_TYPE_DESC;
        return buildPagingMeta(request.getPageNumb(), request.getPageSize(), sortBy, sortOrder, pages, request.getKeyword(), request.getSearchBy());
    }

    private static <T> PagingMeta buildPagingMeta(int pageNumb, int pageSize, String sortBy, String sortOrder, Page<T> pages, String keyword, String searchBy) {
        return new PagingMeta(
                pages.getTotalElements(),
                pages.getTotalPages(),
                pageNumb + CommonConstant.ONE_INT_VALUE,
                pageSize,
                sortBy,
                sortOrder,
                keyword,
                searchBy
        );
    }

    private static <T> PagingMeta buildPagingMeta(int pageNumb, int pageSize, String sortBy, String sortOrder, Page<T> pages) {
        return buildPagingMeta(pageNumb, pageSize, sortBy, sortOrder, pages, null, null);
    }
}
