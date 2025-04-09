package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    boolean existByCode(String code);

    Optional<Author> findByIdAndActiveFlagIsTrue(Long id);
}
