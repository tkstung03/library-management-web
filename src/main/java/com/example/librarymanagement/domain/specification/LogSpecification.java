package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.dto.filter.LogFilter;
import com.example.librarymanagement.domain.entity.Log;
import com.example.librarymanagement.domain.entity.Log_;
import com.example.librarymanagement.domain.entity.User;
import com.example.librarymanagement.domain.entity.User_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class LogSpecification {

    public static Specification<Log> filterLogs(String keyword, String searchBy, LogFilter logFilter) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Log_.USER -> {
                        Join<Log, User> logUserJoin = root.join(Log_.user);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(logUserJoin.get(User_.username), "%" + keyword + "%"));
                    }

                    case Log_.FEATURE ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Log_.feature), "%" + keyword + "%"));

                    case Log_.EVENT ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Log_.event), "%" + keyword + "%"));

                    case Log_.CONTENT ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Log_.content), "%" + keyword + "%"));
                }
            }

            if (logFilter != null) {
                if (logFilter.getStartDate() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(Log_.timestamp), logFilter.getStartDate().atStartOfDay()));
                }
                if (logFilter.getEndDate() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(Log_.timestamp), logFilter.getEndDate().atTime(23, 59, 59)));
                }
            }

            return predicate;
        };
    }
}
