package com.example.librarymanagement.domain.specification;

import com.example.librarymanagement.domain.entity.ExportReceipt;
import com.example.librarymanagement.domain.entity.ExportReceipt_;
import com.example.librarymanagement.util.SpecificationsUtil;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ExportReceiptSpecification {
    public static Specification<ExportReceipt> filterExportReceipts(String keyword, String searchBy) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicate =criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)){
                switch (searchBy) {
                    case ExportReceipt_.ID ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(ExportReceipt_.id),
                                SpecificationsUtil.castToRequiredType(root.get(ExportReceipt_.id).getJavaType(), keyword)));

                    case ExportReceipt_.RECEIPT_NUMBER ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(ExportReceipt_.receiptNumber), "%" + keyword + "%"));

                    case ExportReceipt_.EXPORT_REASON ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(ExportReceipt_.exportReason), "%" + keyword + "%"));
                }
            }

            return predicate;
        };
    }
}
