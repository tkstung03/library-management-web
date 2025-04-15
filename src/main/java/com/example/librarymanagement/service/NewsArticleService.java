package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.newsarticle.NewsArticleRequestDto;
import com.example.librarymanagement.domain.dto.response.newsarticle.NewsArticleResponseDto;
import com.example.librarymanagement.domain.entity.NewsArticle;
import org.springframework.web.multipart.MultipartFile;

public interface NewsArticleService {

    CommonResponseDto save(NewsArticleRequestDto requestDto, MultipartFile imageFile, String userId);

    CommonResponseDto update(Long id, NewsArticleRequestDto requestDto, MultipartFile imageFile, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<NewsArticle> findAll(PaginationFullRequestDto requestDto);

    NewsArticle findById(Long id);

    NewsArticle getNewsArticleByTitleSlug(String titleSlug);

    CommonResponseDto toggleActiveStatus(Long id, String userId);

    PaginationResponseDto<NewsArticleResponseDto> getNewsArticles(PaginationFullRequestDto requestDto);

}
