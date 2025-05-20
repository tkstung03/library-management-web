package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.LibraryVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LibraryVisitRepository extends JpaRepository<LibraryVisit, Long>, JpaSpecificationExecutor<LibraryVisit> {

    //Tìm lần vào thư viện gần nhất của 1 người đọc
    LibraryVisit findTopByReaderIdAndEntryTimeBetweenOrderByEntryTimeDesc(Long readerId, LocalDateTime start, LocalDateTime end);

    //Tìm tất cả các lượt vào thư viện trong khoảng thời gian, chưa có giờ ra
    List<LibraryVisit> findAllByEntryTimeBetweenAndExitTimeIsNull(LocalDateTime start, LocalDateTime end);

    List<LibraryVisit> findByEntryTimeBetween(LocalDateTime from, LocalDateTime to);

}
