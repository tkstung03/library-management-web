package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.BorrowReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface BorrowReceiptRepository extends JpaRepository<BorrowReceipt, Long>, JpaSpecificationExecutor<BorrowReceipt> {
    boolean existsByReceiptNumber(String receiptNumber);

    List<BorrowReceipt> findAllByIdIn(Set<Long> borrowIds);

    @Query("SELECT COUNT(br) FROM BorrowReceipt br WHERE br.status != 'RETURNED'")
    int countCurrentlyBorrowed();

    @Query("SELECT COUNT(br) FROM BorrowReceipt br WHERE br.dueDate = CURRENT_DATE")
    int countDueToday();

    @Query("SELECT COUNT(br) FROM BorrowReceipt br WHERE br.dueDate < CURRENT_DATE AND br.status != 'RETURNED'")
    int countOverdue();

    @Query("SELECT br FROM BorrowReceipt br WHERE br.dueDate < CURRENT_DATE AND br.status != 'RETURNED'")
    List<BorrowReceipt> findAllOverdueRecords();

    @Query("SELECT br FROM BorrowReceipt br WHERE br.dueDate < CURRENT_DATE AND br.dueDate >= :daysAgo AND br.status != 'RETURNED'")
    List<BorrowReceipt> findOverdueRecords(@Param("daysAgo") LocalDate daysAgo);

    @Modifying
    @Transactional
    @Query("UPDATE BorrowReceipt b SET b.status = 'OVERDUE' WHERE b.dueDate < CURRENT_DATE AND b.status NOT IN ('RETURNED', 'OVERDUE')")
    void updateOverdueStatus();
}
