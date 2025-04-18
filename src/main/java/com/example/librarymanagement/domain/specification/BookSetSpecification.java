package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.BookSet;
import com.example.librarymanagement.domain.entity.BookSet_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class BookSetSpecification {

    public static Specification<BookSet> filterBookSets(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)){
                switch (searchBy) {
                    case BookSet_.NAME ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(BookSet_.name), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(BookSet_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }
}
