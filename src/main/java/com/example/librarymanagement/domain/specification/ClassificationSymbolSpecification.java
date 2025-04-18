package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.ClassificationSymbol;
import com.example.librarymanagement.domain.entity.ClassificationSymbol_;
import com.example.librarymanagement.util.SpecificationsUtil;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ClassificationSymbolSpecification {
    public static Specification<ClassificationSymbol> filterClassificationSymbols(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate = criteriaBuilder.conjunction();

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(ClassificationSymbol_.activeFlag), activeFlag));
            }

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case ClassificationSymbol_.NAME ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(ClassificationSymbol_.name),
                                "%" + keyword + "%"));

                    case ClassificationSymbol_.CODE ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(ClassificationSymbol_.code),
                                "%" + keyword + "%"));

                    case ClassificationSymbol_.LEVEL ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(ClassificationSymbol_.level),
                                SpecificationsUtil.castToRequiredType(root.get(ClassificationSymbol_.level).getJavaType(), keyword)));
                }
            }

            return predicate;
        };
    }
}
