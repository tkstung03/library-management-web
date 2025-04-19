package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.UserGroup;
import com.example.librarymanagement.domain.entity.UserGroup_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class UserGroupSpecification {

    public static Specification<UserGroup> filterUserGroups(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(UserGroup_.activeFlag), activeFlag));
            }

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case UserGroup_.CODE ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(UserGroup_.code), "%" + keyword + "%"));

                    case UserGroup_.NAME ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(UserGroup_.name), "%" + keyword + "%"));
                }
            }

            return predicate;
        };
    }
}
