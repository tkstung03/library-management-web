package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.constant.BookCondition;
import com.example.librarymanagement.domain.entity.Book;
import com.example.librarymanagement.domain.entity.BookDefinition;
import com.example.librarymanagement.domain.entity.BookDefinition_;
import com.example.librarymanagement.domain.entity.Book_;
import com.example.librarymanagement.util.SpecificationsUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> filterBooks(String keyword, String searchBy, BookCondition condition) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            //Lay sach chua xuat/con trong thu vien
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get(Book_.exportReceipt)));

            if (condition != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Book_.bookCondition), condition));
            }

            if(StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Book_.ID ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Book_.id),
                                SpecificationsUtil.castToRequiredType(root.get(Book_.id).getJavaType(), keyword)));

                    case Book_.BOOK_CODE ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(Book_.bookCode), "%" + keyword + "%"));

                    case BookDefinition_.TITLE -> {
                        Join<Book, BookDefinition> bookDefinitionJoin = root.join(Book_.bookDefinition, JoinType.INNER);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(bookDefinitionJoin.get(BookDefinition_.title), "%" + keyword + "%"));
                    }
                }
            }

            return predicate;
        };
    }
}
