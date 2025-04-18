package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.CategoryGroup;
import com.example.librarymanagement.domain.entity.CategoryGroup_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class CategoryGroupSpecification {
    public static Specification<CategoryGroup> filterCategoryGroups(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate = criteriaBuilder.conjunction();

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(CategoryGroup_.activeFlag), activeFlag));
            }

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case CategoryGroup_.GROUP_NAME ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(CategoryGroup_.GROUP_NAME), "%" + keyword + "%"));
                }
            }

            return predicate;
        };
    }
}
