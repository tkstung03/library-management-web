package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.LibraryVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT lv FROM LibraryVisit lv " +
            "JOIN FETCH lv.reader r " +
            "JOIN FETCH r.major m")
    List<LibraryVisit> findAllWithReaderAndMajor();

    @EntityGraph(attributePaths = {"reader", "reader.major"})
    Page<LibraryVisit> findAll(Specification<LibraryVisit> spec, Pageable pageable);

    @Query("SELECT lv FROM LibraryVisit lv " +
            "JOIN lv.reader r " +
            "WHERE lv.entryTime BETWEEN :start AND :end " +
            "AND r.major.id = :majorId")
    List<LibraryVisit> findByEntryTimeBetweenAndMajorId(
            LocalDateTime start,
            LocalDateTime end,
            Long majorId
    );

}
