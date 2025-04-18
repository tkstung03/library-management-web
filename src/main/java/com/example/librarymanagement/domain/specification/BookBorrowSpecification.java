package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.constant.BookBorrowStatus;
import com.example.librarymanagement.domain.dto.filter.TimeFilter;
import com.example.librarymanagement.domain.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class BookBorrowSpecification {
    public static Specification<BookBorrow> filterBookBorrows(List<BookBorrowStatus> status) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (status != null && status.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get(BookBorrow_.bookBorrowStatus).in(status));
            }

            return predicate;
        });
    }

    public static Specification<BookBorrow> filterBookBorrows(TimeFilter timeFilter) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();
            if (timeFilter != null) {
                if (timeFilter.getStartDate() != null) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.greaterThanOrEqualTo(root.get(BookBorrow_.returnDate),
                                    timeFilter.getStartDate()));
                }

                if (timeFilter.getEndDate() != null) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.lessThanOrEqualTo(root.get(BookBorrow_.returnDate),
                                    timeFilter.getEndDate()));
                }
            }
            return predicate;
        };
    }

    public static Specification<BookBorrow> filterBookBorrows(String keyword, String searchBy) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case BorrowReceipt_.RECEIPT_NUMBER -> {
                        Join<BookBorrow, BorrowReceipt> borrowReceiptJoin =root.join(BookBorrow_.borrowReceipt);
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(borrowReceiptJoin.get(BorrowReceipt_.receiptNumber),
                                        "%" + keyword + "%"));
                    }

                    case Reader_.FULL_NAME -> {
                        Join<BookBorrow, BorrowReceipt> borrowReceiptJoin = root.join(BookBorrow_.borrowReceipt);
                        Join<BorrowReceipt, Reader> readerJoin = borrowReceiptJoin.join(BorrowReceipt_.reader);
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(readerJoin.get(Reader_.fullName),
                                        "%" + keyword + "%"));
                    }

                    case BookDefinition_.TITLE -> {
                        Join<BookBorrow, Book> bookJoin = root.join(BookBorrow_.book);
                        Join<Book, BookDefinition> bookDefinitionJoin = bookJoin.join(Book_.bookDefinition);
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(bookDefinitionJoin.get(BookDefinition_.title),
                                        "%" + keyword + "%"));
                    }
                }
            }

            return predicate;
        };
    }
}
