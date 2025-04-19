package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.NewsArticle;
import com.example.librarymanagement.domain.entity.NewsArticle_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class NewsArticleSpecification {

    public static Specification<NewsArticle> filterNewsArticles(String keyword, String searchBy, Boolean activeFlag){
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)){
                switch (searchBy) {
                    case NewsArticle_.TITLE ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(NewsArticle_.title), "%" + keyword + "%"));

                    case NewsArticle_.DESCRIPTION ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(NewsArticle_.description), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(NewsArticle_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }
}
