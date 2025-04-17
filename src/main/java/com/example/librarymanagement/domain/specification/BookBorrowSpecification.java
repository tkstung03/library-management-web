package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.constant.BookBorrowStatus;
import com.example.librarymanagement.domain.entity.BookBorrow;
import com.example.librarymanagement.domain.entity.BookBorrow_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class BookBorrowSpecification {
    public static Specification<BookBorrow> filterBookBorrows(List<BookBorrowStatus> status){
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (status != null && status.isEmpty()){
                predicate = criteriaBuilder.and(predicate, root.get(BookBorrow_.bookBorrowStatus).in(status));
            }

            return predicate;
        });
    }
}
