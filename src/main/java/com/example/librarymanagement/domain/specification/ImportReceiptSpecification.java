package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.ImportReceipt;
import com.example.librarymanagement.domain.entity.ImportReceipt_;
import com.example.librarymanagement.util.SpecificationsUtil;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ImportReceiptSpecification {
    public static Specification<ImportReceipt> filterImportReceipts(String keyword, String searchBy) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case ImportReceipt_.ID ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(ImportReceipt_.id),
                                    SpecificationsUtil.castToRequiredType(root.get(ImportReceipt_.id).getJavaType(), keyword)));

                    case ImportReceipt_.RECEIPT_NUMBER ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(ImportReceipt_.receiptNumber), "%" + keyword + "%"));

                    case ImportReceipt_.FUNDING_SOURCE ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(ImportReceipt_.fundingSource), "%" + keyword + "%"));

                    case ImportReceipt_.IMPORT_REASON ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(ImportReceipt_.importReason), "%" + keyword + "%"));

                    case ImportReceipt_.GENERAL_RECORD_NUMBER ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(ImportReceipt_.generalRecordNumber), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

}
