package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.User;
import com.example.librarymanagement.domain.entity.User_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> filterUsers(String keyword, String searchBy) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case User_.USERNAME ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(User_.username), "%" + keyword + "%"));

                    case User_.FULL_NAME ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(User_.fullName), "%" + keyword + "%"));
                }
            }

            return predicate;
        };
    }
}
