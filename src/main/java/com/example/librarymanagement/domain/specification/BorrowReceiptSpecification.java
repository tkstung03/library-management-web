package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.constant.BorrowStatus;
import com.example.librarymanagement.domain.entity.BorrowReceipt;
import com.example.librarymanagement.domain.entity.BorrowReceipt_;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.entity.Reader_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class BorrowReceiptSpecification {

    public static Specification<BorrowReceipt> filterBorrowReceipts(String keyword, String searchBy, BorrowStatus status) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate = criteriaBuilder.conjunction();

            if (status != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(BorrowReceipt_.status), status));
            }

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case BorrowReceipt_.RECEIPT_NUMBER ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(BorrowReceipt_.receiptNumber), "%" + keyword + "%"));

                    case Reader_.FULL_NAME -> {
                        Join<BorrowReceipt, Reader> readerJoin = root.join(BorrowReceipt_.reader);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(readerJoin.get(Reader_.fullName), "%" + keyword + "%"));
                    }

                    case Reader_.CARD_NUMBER -> {
                        Join<BorrowReceipt, Reader> readerJoin = root.join(BorrowReceipt_.reader);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(readerJoin.get(Reader_.cardNumber), "%" + keyword + "%"));
                    }
                }
            }

            return predicate;
        };
    }

    public static Specification<BorrowReceipt> filterBorrowReceiptByReader(String cardNumber) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate = criteriaBuilder.conjunction();

            Join<BorrowReceipt, Reader> readerJoin = root.join(BorrowReceipt_.reader, JoinType.INNER);
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(readerJoin.get(Reader_.cardNumber), cardNumber));

            return predicate;
        };
    }
}
