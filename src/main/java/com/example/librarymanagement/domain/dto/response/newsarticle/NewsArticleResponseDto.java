package com.example.librarymanagement.domain.dto.response.newsarticle;

import com.example.librarymanagement.domain.entity.NewsArticle;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Getter
public class NewsArticleResponseDto {
    private final long id;

    private final String imageUrl;

    private final LocalDate createdDate;

    private final String title;

    private final String titleSlug;

    private final String description;

    private final String author;

    public NewsArticleResponseDto(NewsArticle newsArticle){
        this.id = newsArticle.getId();
        this.imageUrl = newsArticle.getImageUrl();
        this.createdDate = newsArticle.getCreatedDate();
        this.title = newsArticle.getTitle();
        this.titleSlug = newsArticle.getTitleSlug();
        this.description = newsArticle.getDescription();
        this.author = newsArticle.getUser().getFullName();
    }
}
