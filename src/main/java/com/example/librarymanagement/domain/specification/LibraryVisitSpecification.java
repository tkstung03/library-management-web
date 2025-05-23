package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.dto.filter.LibraryVisitFilter;
import com.example.librarymanagement.domain.entity.LibraryVisit;
import com.example.librarymanagement.domain.entity.LibraryVisit_;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.entity.Reader_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Library;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LibraryVisitSpecification {

    public static Specification<LibraryVisit> filterLibraryVisits(String keyword, String searchBy, LibraryVisitFilter filter) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate = criteriaBuilder.conjunction();

            if (filter == null || (filter.getStartDate() == null && filter.getEndDate() == null)) {
                LocalDate today = LocalDate.now();
                LocalDateTime startOfDay = today.atStartOfDay();
                LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

                //Dieu kien loc theo thoi gian trong ngay hom nay
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.between(root.get(LibraryVisit_.entryTime), startOfDay, endOfDay));
            }
            else {
                if (filter.getStartDate() != null) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.greaterThanOrEqualTo(root.get(LibraryVisit_.entryTime), filter.getStartDate().atStartOfDay()));
                }
                if (filter.getEndDate() != null) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.lessThanOrEqualTo(root.get(LibraryVisit_.entryTime), filter.getEndDate().atTime(23,59,59)));
                }
            }

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Reader_.CARD_NUMBER -> {
                        Join<LibraryVisit, Reader> readerJoin = root.join(LibraryVisit_.reader);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(readerJoin.get(Reader_.cardNumber), "%" + keyword + "%"));
                    }

                    case Reader_.FULL_NAME -> {
                        Join<LibraryVisit, Reader> readerJoin = root.join(LibraryVisit_.reader);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(readerJoin.get(Reader_.fullName), "%" + keyword + "%"));
                    }
                }
            }
            return predicate;
        };
    }
}
