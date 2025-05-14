package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.ReaderViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReaderViolationRepository extends JpaRepository<ReaderViolation, Long>, JpaSpecificationExecutor<ReaderViolation> {

}
