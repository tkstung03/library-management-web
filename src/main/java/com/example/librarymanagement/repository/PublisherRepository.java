package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long>, JpaSpecificationExecutor<Publisher> {

    boolean existsByCode(String code);

    Optional<Publisher> findByIdAndActiveFlagIsTrue(Long id);
}
