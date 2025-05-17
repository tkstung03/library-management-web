package com.example.librarymanagement.constant;

public class ErrorMessage {

    public static final String ERR_EXCEPTION_GENERAL = "exception.general";
    public static final String ERR_UNAUTHORIZED = "exception.unauthorized";
    public static final String ERR_FORBIDDEN = "exception.forbidden";
    public static final String ERR_FORBIDDEN_UPDATE_DELETE = "exception.forbidden.update-delete";
    public static final String ERR_METHOD_NOT_SUPPORTED = "exception.method-not-supported";
    public static final String ERR_REQUEST_BODY = "exception.required-request-body-missing";
    public static final String ERR_REQUIRED_MISSING_PARAMETER = "exception.missing-servlet-request-parameter";
    public static final String ERR_METHOD_ARGUMENT_TYPE_MISMATCH = "exception.method-argument-type-mismatch";
    public static final String ERR_RESOURCE_NOT_FOUND = "exception.resource-not-found";
    public static final String ERR_MISSING_SERVLET_REQUEST_PART = "exception.request.part.missing";
    public static final String ERR_UNSUPPORTED_MEDIA_TYPE = "exception.unsupported.media.type";
    public static final String ERR_MULTIPART_EXCEPTION = "exception.multipart";
    public static final String ERR_ILLEGAL_ARGUMENT = "exception.illegal-arguments";

    public static final String INVALID_SOME_THING_FIELD = "invalid.general";
    public static final String INVALID_FORMAT_SOME_THING_FIELD = "invalid.general.format";
    public static final String INVALID_NUMBER_FORMAT = "invalid.general.number-format";
    public static final String INVALID_SOME_THING_FIELD_IS_REQUIRED = "invalid.general.required";
    public static final String INVALID_ARRAY_IS_REQUIRED = "invalid.array.required";
    public static final String INVALID_ARRAY_NOT_EMPTY = "invalid.array.not.empty";
    public static final String INVALID_ARRAY_LENGTH = "invalid.array.length";
    public static final String INVALID_NOT_BLANK_FIELD = "invalid.general.not-blank";
    public static final String INVALID_NOT_NULL_FIELD = "invalid.general.not-null";
    public static final String INVALID_FORMAT_NAME = "invalid.name-format";
    public static final String INVALID_FORMAT_USERNAME = "invalid.username-format";
    public static final String INVALID_FORMAT_PASSWORD = "invalid.password-format";
    public static final String INVALID_REPEAT_PASSWORD = "invalid.password-repeat";
    public static final String INVALID_FORMAT_EMAIL = "invalid.email-format";
    public static final String INVALID_FORMAT_PHONE = "invalid.phone-format";
    public static final String INVALID_COORDINATES = "invalid.coordinates";
    public static final String INVALID_MINIMUM_ONE = "invalid.minimum-one";
    public static final String INVALID_MINIMUM_ZERO = "invalid.minimum-zero";
    public static final String INVALID_MAXIMUM_ONE_HUNDRED = "invalid.maximum-one-hundred";
    public static final String INVALID_MAXIMUM_FIVE = "invalid.maximum-five";
    public static final String INVALID_MAXIMUM_INT = "invalid.maximum-int";
    public static final String INVALID_MAXIMUM_SHORT = "invalid.maximum-short";
    public static final String INVALID_TEXT_LENGTH = "invalid.text.length";
    public static final String INVALID_KEYWORD_LENGTH = "invalid.keyword.length";

    //Date
    public static final String INVALID_DATE = "invalid.date-format";
    public static final String INVALID_DATE_FEATURE = "invalid.date-future";
    public static final String INVALID_DATE_PAST = "invalid.date-past";
    public static final String INVALID_TIME = "invalid.time-format";
    public static final String INVALID_LOCAL_DATE_FORMAT = "invalid.local-date-format";
    public static final String INVALID_LOCAL_DATE_TIME_FORMAT = "invalid.local-date-time-format";
    //File
    public static final String INVALID_MAX_UPLOAD_SIZE_EXCEEDED = "invalid.max-upload-size-exceeded";
    public static final String INVALID_FILE_REQUIRED = "invalid.file.required";
    public static final String INVALID_FILE_SIZE = "invalid.file.size";
    public static final String INVALID_FILE_TYPE = "invalid.file.type";
    public static final String INVALID_URL_FORMAT = "invalid.url-format";
    public static final String INVALID_OPERATOR_NOT_SUPPORTED = "invalid.operator.not-supported";

    public static class Auth {
        public static final String ERR_INCORRECT_USERNAME_PASSWORD = "exception.auth.incorrect.username-password";
        public static final String ERR_INCORRECT_PASSWORD = "exception.auth.incorrect.password";
        public static final String ERR_INCORRECT_EMAIL = "exception.auth.incorrect.email";
        public static final String ERR_DUPLICATE_USERNAME = "exception.auth.duplicate.username";
        public static final String ERR_DUPLICATE_EMAIL = "exception.auth.duplicate.email";
        public static final String ERR_DUPLICATE_USERNAME_EMAIL = "exception.auth.duplicate.username.email";
        public static final String ERR_ACCOUNT_NOT_ENABLED = "exception.auth.account.not.enabled";
        public static final String ERR_ACCOUNT_LOCKED = "exception.auth.account.locked";
        public static final String ERR_INVALID_REFRESH_TOKEN = "exception.auth.invalid.refresh.token";
        public static final String EXPIRED_REFRESH_TOKEN = "exception.auth.expired.refresh.token";
        public static final String INVALID_ACCESS_TOKEN = "exception.auth.invalid.access.token";
        public static final String USER_NOT_FOUND = "exception.auth.user.not.found";
        public static final String ERR_ACCOUNT_NOT_ACTIVE = "exception.auth.account.not.active";
        public static final String ERR_ACCOUNT_EXPIRED = "exception.auth.account.exp";
    }

    public static class Role {
        public static final String ERR_NOT_FOUND_ID = "exception.role.not.found.id";
        public static final String ERR_NOT_FOUND_NAME = "exception.role.not.found.name";
    }

    public static class User {
        public static final String ERR_NOT_FOUND_USERNAME_OR_EMAIL = "exception.user.not.found.username-email";
        public static final String ERR_NOT_FOUND_USERNAME = "exception.user.not.found.username";
        public static final String ERR_NOT_FOUND_EMAIL = "exception.user.not.found.email";
        public static final String ERR_NOT_FOUND_ID = "exception.user.not.found.id";
        public static final String ERR_NOT_FOUND_ACCOUNT = "exception.user.not.found.account";
        public static final String ALREADY_VERIFIED = "exception.user.already.verified";
        public static final String INVALID_VERIFICATION_CODE = "exception.user.invalid.verification.code";
        public static final String RATE_LIMIT = "exception.user.rate.limit";
        public static final String INVALID_EMAIL = "exception.user.invalid.email";
        public static final String ERR_NOT_ALLOWED_SUPER_ADMIN = "exception.user.not.allowed.super.admin";
        public static final String ERR_DUPLICATE_USERNAME_EMAIL = "exception.user.duplicate.username.email";
    }

    public static class Reader {
        public static final String ERR_NOT_FOUND_ID = "exception.reader.not.found.id";
        public static final String ERR_NOT_FOUND_CARD_NUMBER = "exception.reader.not.found.card.number";
        public static final String ERR_DUPLICATE_CARD_NUMBER = "exception.reader.duplicate.card.number";
        public static final String ERR_DUPLICATE_EMAIL = "exception.reader.duplicate.email";
        public static final String ERR_READER_HAS_DATA = "exception.reader.has.data";
        public static final String ERR_READER_INACTIVE = "exception.reader.inactive";
        public static final String ERR_READER_SUSPENDED = "exception.reader.suspended";
        public static final String ERR_READER_REVOKED = "exception.reader.revoked";
        public static final String ERR_READER_EXPIRED = "exception.reader.expired";
    }

    public static class ReaderViolation {
        public static final String ERR_NOT_FOUND_ID = "exception.reader.violation.not.found.id";
    }

    public static class Author {
        public static final String ERR_DUPLICATE_CODE = "exception.author.duplicate.code";
        public static final String ERR_NOT_FOUND_ID = "exception.author.not.found.id";
        public static final String ERR_HAS_LINKED_BOOKS = "exception.author.has.linked";
    }

    public static class Category {
        public static final String ERR_DUPLICATE_NAME = "exception.category.duplicate.name";
        public static final String ERR_DUPLICATE_CODE = "exception.category.duplicate.code";
        public static final String ERR_NOT_FOUND_ID = "exception.category.not.found.id";
        public static final String ERR_HAS_LINKED_BOOKS = "exception.category.has.linked";
    }

    public static class CategoryGroup {
        public static final String ERR_NOT_FOUND_ID = "exception.category.group.not.found.id";
        public static final String ERR_DUPLICATE_GROUP_NAME = "exception.category.group.duplicate.name";
        public static final String ERR_HAS_LINKED_CATEGORIES = "exception.category.group.has.linked";
    }

    public static class BookSet {
        public static final String ERR_NOT_FOUND_ID = "exception.book.set.not.found.id";
        public static final String ERR_DUPLICATE_NAME = "exception.book.set.duplicate.name";
        public static final String ERR_HAS_LINKED_BOOKS = "exception.book.set.has.linked";
    }

    public static class Publisher {
        public static final String ERR_NOT_FOUND_ID = "exception.publisher.not.found.id";
        public static final String ERR_DUPLICATE_CODE = "exception.publisher.duplicate.code";
        public static final String ERR_HAS_LINKED_BOOKS = "exception.publisher.has.linked";
    }

    public static class BookDefinition {
        public static final String ERR_NOT_FOUND_ID = "exception.book.definition.not.found.id";
        public static final String ERR_HAS_LINKED_BOOKS = "exception.book.definition.has.linked";
        public static final String ERR_DUPLICATE_CODE = "exception.book.definition.duplicate.code";
        public static final String ERR_INVALID_FIELD = "exception.book.definition.invalid.field";
        public static final String ERR_OUT_OF_STOCK = "exception.book.definition.out.of.stock";
    }

    public static class ClassificationSymbol {
        public static final String ERR_NOT_FOUND_ID = "exception.classification.symbol.not.found.id";
        public static final String ERR_DUPLICATE_CODE = "exception.classification.symbol.duplicate.code";
        public static final String ERR_HAS_LINKED_BOOKS = "exception.classification.symbol.has.linked";
    }

    public static class ImportReceipt {
        public static final String ERR_NOT_FOUND_ID = "exception.import.receipt.not.found.id";
        public static final String ERR_DUPLICATE_NUMBER = "exception.import.receipt.duplicate.code";
    }

    public static class ExportReceipt {
        public static final String ERR_NOT_FOUND_ID = "exception.export.receipt.not.found.id";
        public static final String ERR_DUPLICATE_NUMBER = "exception.export.receipt.duplicate.code";
    }

    public static class NewsArticle {
        public static final String ERR_NOT_FOUND_ID = "exception.news.not.found.id";
    }

    public static class UserGroup {
        public static final String ERR_NOT_FOUND_ID = "exception.user.group.not.found.id";
        public static final String ERR_DUPLICATE_CODE = "exception.user.group.duplicate.code";
        public static final String ERR_HAS_LINKED_USERS = "exception.user.group.has.linked";
    }

    public static class Book {
        public static final String ERR_NOT_FOUND_ID = "exception.book.not.found.id";
        public static final String ERR_NOT_FOUND_CODE = "exception.book.not.found.code";
        public static final String ERR_HAS_LINKED = "exception.book.has.linked";
        public static final String ERR_HAS_LINKED_EXPORT_RECEPTION = "exception.book.has.linked.exported";
        public static final String ERR_BOOK_ALREADY_BORROWED = "exception.book.already.borrowed";
        public static final String ERR_BOOK_RESERVED_BY_ANOTHER_READER = "exception.book.reserved.by.another.reader";
        public static final String ERR_BOOK_ALREADY_ON_LOAN = "exception.book.already.on-loan";
    }

    public static class BorrowReceipt {
        public static final String ERR_NOT_FOUND_ID = "exception.borrow.receipt.not.found.id";
        public static final String ERR_NOT_FOUND_OVERDUE = "exception.borrow.receipt.not.found.overdue";
        public static final String ERR_DUPLICATE_RECEIPT_NUMBER = "exception.borrow.receipt.duplicate.number";
    }

    public static class BookBorrow {
        public static final String ERR_NOT_FOUND_ID = "exception.book.borrow.not.found.id";
        public static final String ERR_NOT_FOUND_IDS = "exception.book.borrow.not.found.ids";
        public static final String ERR_NOT_RETURNED_IN_ANOTHER_RECEIPT = "exception.book.borrow.not.returned.in.another.receipt";
    }

    public static class Cart {
        public static final String ERR_NOT_FOUND_ID = "exception.cart.not.found.id";
        public static final String ERR_MAX_BOOKS_IN_CART = "exception.cart.max.books.exceeded";
    }

    public static class Review {
        public static final String ERR_NOT_FOUND_ID = "exception.review.not.found.id";
        public static final String ERR_ALREADY_REVIEWED = "exception.review.already_reviewed";
    }

    public static class Notification {
        public static final String ERR_NOT_FOUND_ID = "exception.notification.not.found.id";
    }
}
