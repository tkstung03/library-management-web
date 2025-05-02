package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.NewsArticle;
import com.example.librarymanagement.domain.mapper.NewsArticleMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long>, JpaSpecificationExecutor<NewsArticle> {

    Optional<NewsArticle> findByTitleSlug(String titleSlug);
}
