package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.Author;
import com.example.librarymanagement.domain.entity.Author_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecification {

    public static Specification<Author> filterAuthors(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Author_.CODE -> {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(root.get(Author_.code),
                                        "%" + keyword + "%"));
                    }

                    case Author_.FULL_NAME -> {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(root.get(Author_.fullName),
                                        "%" + keyword + "%"));
                    }
                }
            }

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get(Author_.activeFlag),
                                activeFlag));
            }

            return predicate;
        };
    }
}
