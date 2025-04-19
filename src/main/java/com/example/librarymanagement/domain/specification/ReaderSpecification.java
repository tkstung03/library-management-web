package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.constant.CardStatus;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.entity.Reader_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ReaderSpecification {

    public static Specification<Reader> filterReaders(String keyword, String searchBy) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Reader_.CARD_NUMBER ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Reader_.cardNumber), "%" + keyword + "%"));

                    case Reader_.FULL_NAME ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Reader_.fullName), "%" + keyword + "%"));

                    case Reader_.STATUS -> {
                        try {
                            CardStatus statusEnum = CardStatus.valueOf(keyword.toUpperCase());
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Reader_.status), statusEnum));
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            }

            return predicate;
        };
    }
}
