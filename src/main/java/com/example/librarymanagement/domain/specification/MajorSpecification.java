package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.Major;
import com.example.librarymanagement.domain.entity.Major_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class MajorSpecification {
    public static Specification<Major> filterMajors(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Major_.NAME ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Major_.name), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Major_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }
}
