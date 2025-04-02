package com.example.librarymanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news_articles")
public class NewsArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_article_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title; //Tiêu đề

    @Column(name = "title_slug", unique = true, nullable = false)
    private String titleSlug; //Tiêu dề phụ

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "active_flag", nullable = false)
    private Boolean activeFlag = Boolean.TRUE;

    @Column(name = "news_type", nullable = false)
    private String newsType;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;
}
