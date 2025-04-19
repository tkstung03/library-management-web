package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.constant.PunishmentForm;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.entity.ReaderViolation;
import com.example.librarymanagement.domain.entity.ReaderViolation_;
import com.example.librarymanagement.domain.entity.Reader_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ReaderViolationSpecification {

    public static Specification<ReaderViolation> filterReaderViolations(String keyword, String searchBy) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Reader_.CARD_NUMBER ->{
                        Join<ReaderViolation, Reader> readerJoin = root.join(ReaderViolation_.reader);
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(readerJoin.get(Reader_.cardNumber), "%" + keyword + "%"));
                    }

                    case Reader_.FULL_NAME -> {
                        Join<ReaderViolation, Reader> readerJoin = root.join(ReaderViolation_.reader);
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(readerJoin.get(Reader_.fullName), "%" + keyword + "%"));
                    }

                    case ReaderViolation_.PUNISHMENT_FORM -> {
                        try {
                            PunishmentForm punishmentFormEnum = PunishmentForm.valueOf(keyword.toUpperCase());
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(ReaderViolation_.punishmentForm), punishmentFormEnum));
                        } catch (IllegalArgumentException ignored){
                        }
                    }
                }
            }

            return predicate;
        };
    }
}
