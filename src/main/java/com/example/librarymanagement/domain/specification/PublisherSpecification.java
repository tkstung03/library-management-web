package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.Publisher;
import com.example.librarymanagement.domain.entity.Publisher_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class PublisherSpecification {

    public static Specification<Publisher> filterPublishers(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Publisher_.CODE ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Publisher_.code), "%" + keyword + "%"));

                    case Publisher_.NAME ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Publisher_.name), "%" + keyword + "%"));

                    case Publisher_.ADDRESS ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Publisher_.address), "%" + keyword + "%"));

                    case Publisher_.CITY ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Publisher_.city), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Publisher_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }
}
