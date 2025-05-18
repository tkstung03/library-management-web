package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.QueryOperator;
import com.example.librarymanagement.domain.dto.filter.BookDefinitionFilter;
import com.example.librarymanagement.domain.dto.filter.QueryFilter;
import com.example.librarymanagement.domain.entity.*;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.util.SpecificationsUtil;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

import static com.example.librarymanagement.util.SpecificationsUtil.castToRequiredType;

public class BookDefinitionSpecification {

    private static final Map<String, List<QueryOperator>> ALLOWED_FIELDS = Map.of(
            "title", List.of(QueryOperator.LIKE, QueryOperator.EQUALS, QueryOperator.IN),
            "author", List.of(QueryOperator.LIKE, QueryOperator.EQUALS, QueryOperator.IN),
            "publisher", List.of(QueryOperator.LIKE, QueryOperator.EQUALS, QueryOperator.IN),
            "publishingYear", List.of(QueryOperator.LIKE, QueryOperator.EQUALS, QueryOperator.LESS_THAN),
            "isbn", List.of(QueryOperator.LIKE, QueryOperator.EQUALS)
    );

    public static Specification<BookDefinition> baseFilterBookDefinitions(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(searchBy) && StringUtils.isNotBlank(keyword)) {
                switch (searchBy) {
                    case BookDefinition_.TITLE -> {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(root.get(BookDefinition_.title), "%" + keyword + "%"));
                    }

                    case BookDefinition_.BOOK_NUMBER -> {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(root.get(BookDefinition_.bookNumber),
                                        "%" + keyword + "%"));
                    }
                }
            }

            if (activeFlag != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(BookDefinition_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }

    public static Specification<BookDefinition> filterByCategoryGroupId(Long categoryGroupId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryGroupId != null) {
                Join<BookDefinition, Category> definitionCategoryJoin = root.join(BookDefinition_.category, JoinType.INNER);
                Join<Category, CategoryGroup> categoryGroupJoin = definitionCategoryJoin.join(Category_.categoryGroup, JoinType.INNER);
                return criteriaBuilder.equal(categoryGroupJoin.get(CategoryGroup_.id), categoryGroupId);
            }

            return criteriaBuilder.conjunction();

        };
    }

    public static Specification<BookDefinition> filterByAuthorId(Long authorId) {
        return (root, query, criteriaBuilder) -> {
            if (authorId != null) {
                Join<BookDefinition, BookAuthor> bookAuthorJoin = root.join(BookDefinition_.bookAuthors, JoinType.INNER);
                Join<BookAuthor, Author> authorJoin = bookAuthorJoin.join(BookAuthor_.author, JoinType.INNER);
                return criteriaBuilder.equal(authorJoin.get(Author_.id), authorId);
            }

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<BookDefinition> filterByBooksCountGreaterThanZero() {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Book> bookRoot = subquery.from(Book.class);
            subquery.select(criteriaBuilder.literal(1L));

            subquery.where(criteriaBuilder.equal(bookRoot.get(Book_.bookDefinition), root));

            return criteriaBuilder.exists(subquery);
        };
    }

    public static Specification<BookDefinition> filterByCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId != null) {
                Join<BookDefinition, Category> categoryJoin = root.join(BookDefinition_.category, JoinType.INNER);
                return criteriaBuilder.equal(categoryJoin.get(Category_.id), categoryId);
            }

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<BookDefinition> orderByBorrowCount() {
        return (root, query, criteriaBuilder) -> {
            Join<BookDefinition, Book> bookJoin = root.join(BookDefinition_.books, JoinType.LEFT);
            Join<Book, BookBorrow> bookBorrowJoin = bookJoin.join(Book_.bookBorrows, JoinType.LEFT);

            query.groupBy(root);
            query.orderBy(criteriaBuilder.desc(criteriaBuilder.count(bookBorrowJoin.get(BookBorrow_.id))));

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<BookDefinition> orderByNewReleases() {
        return (root, query, criteriaBuilder) -> {
            Expression<Integer> yearAsNumber = criteriaBuilder.function("TRY_CAST", Integer.class, root.get(BookDefinition_.publishingYear));
            query.orderBy(criteriaBuilder.desc(yearAsNumber));

            return criteriaBuilder.conjunction();
        };
    }

    //Validate du lieu
    private static void validateField(String fieldName, QueryOperator operator) {
        if (!ALLOWED_FIELDS.containsKey(fieldName)) {
            throw new BadRequestException(ErrorMessage.BookDefinition.ERR_INVALID_FIELD, fieldName);
        }

        if (!ALLOWED_FIELDS.get(fieldName).contains(operator)) {
            throw new BadRequestException(ErrorMessage.INVALID_OPERATOR_NOT_SUPPORTED, operator, fieldName);
        }
    }

    public static Specification<BookDefinition> createSpecification(QueryFilter input) {
        return (root, query, criteriaBuilder) -> {
            String fieldName = input.getField();
            QueryOperator operator = input.getOperator();
            validateField(fieldName, operator);

            Path<?> path = null;

            if ("author".equalsIgnoreCase(fieldName)) {
                Join<BookDefinition, BookAuthor> bookAuthorJoin = root.join(BookDefinition_.bookAuthors, JoinType.LEFT);
                Join<BookAuthor, Author> authorJoin = bookAuthorJoin.join(BookAuthor_.author, JoinType.LEFT);
                path = authorJoin.get(Author_.fullName);
            }
            if ("publisher".equalsIgnoreCase(fieldName)) {
                Join<BookDefinition, Publisher> publisherJoin = root.join(BookDefinition_.publisher, JoinType.LEFT);
                path = publisherJoin.get(Publisher_.name);
            }

            if (path == null) {
                path = root.get(fieldName);
            }

            return switch (operator) {
                case EQUALS -> criteriaBuilder.equal(path, castToRequiredType(path.getJavaType(), input.getValue()));

                case NOT_EQUALS ->
                        criteriaBuilder.notEqual(path, castToRequiredType(path.getJavaType(), input.getValue()));

                case GREATER_THAN ->
                        criteriaBuilder.gt(path.as(Number.class), (Number) castToRequiredType(path.getJavaType(), input.getValue()));

                case LESS_THAN ->
                        criteriaBuilder.lt(path.as(Number.class), (Number) castToRequiredType(path.getJavaType(), input.getValue()));

                case LIKE -> criteriaBuilder.like(path.as(String.class), "%" + input.getValue() + "%");

                case IN -> path.in(SpecificationsUtil.castListToRequiredType2(path.getJavaType(), input.getValues()));

            };
        };
    }

    public static Specification<BookDefinition> getSpecificationFromFilters(List<QueryFilter> queryFilters) {
        if (queryFilters.isEmpty()) {
            return null;
        }

        Specification<BookDefinition> specification = Specification.where(createSpecification(queryFilters.get(0)));

        for (int i = 1; i < queryFilters.size(); i++) {
            QueryFilter currentQueryFilter = queryFilters.get(i);
            QueryFilter previousQueryFilter = queryFilters.get(i - 1);
            Specification<BookDefinition> nextSpecification = createSpecification(currentQueryFilter);

            if (previousQueryFilter.getJoinType() == com.example.librarymanagement.constant.JoinType.OR) {
                specification = specification.or(nextSpecification);
            } else {
                specification = specification.and(nextSpecification);
            }
        }
        return specification;
    }

    public static Specification<BookDefinition> filterBookDefinitions(BookDefinitionFilter filter) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate = criteriaBuilder.conjunction();

            if (filter.getBookCode() != null && StringUtils.isNotBlank(filter.getBookCode())) {
                Join<BookDefinition, Book> bookJoin = root.join(BookDefinition_.books, JoinType.LEFT);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(bookJoin.get(Book_.bookCode), filter.getBookCode()));
            }

            if (filter.getTitle() != null && StringUtils.isNotBlank(filter.getTitle())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(BookDefinition_.title), "%" + filter.getTitle() + "%"));
            }

            if (filter.getKeyword() != null && StringUtils.isNotBlank(filter.getKeyword())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(BookDefinition_.keywords), "%" + filter.getKeyword() + "%"));
            }

            if (filter.getPublishingYear() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(BookDefinition_.publishingYear), filter.getPublishingYear()));
            }

            if (filter.getAuthor() != null && StringUtils.isNotBlank(filter.getAuthor())) {
                Join<BookDefinition, BookAuthor> bookAuthorJoin = root.join(BookDefinition_.bookAuthors, JoinType.LEFT);
                Join<BookAuthor, Author> authorJoin = bookAuthorJoin.join(BookAuthor_.author, JoinType.LEFT);

                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(authorJoin.get(Author_.fullName), "%" + filter.getAuthor() + "%"));
            }

            return predicate;
        };
    }
}
