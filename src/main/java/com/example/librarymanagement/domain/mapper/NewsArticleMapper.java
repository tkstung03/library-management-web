package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.newsarticle.NewsArticleRequestDto;
import com.example.librarymanagement.domain.entity.NewsArticle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsArticleMapper {
    NewsArticle toNewsArticle(NewsArticleRequestDto requestDto);

}
