package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.BookAuthor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    @Modifying
    @Transactional
    void deleteAllByBookDefinitionId(Long id);
}
