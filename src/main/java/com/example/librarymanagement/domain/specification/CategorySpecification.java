package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.Category;
import com.example.librarymanagement.domain.entity.Category_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> filterCategories(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Category_.activeFlag), activeFlag));
            }

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Category_.CATEGORY_NAME ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Category_.categoryName) , "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }
}
