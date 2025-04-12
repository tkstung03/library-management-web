package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.ClassificationSymbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassificationSymbolRepository extends JpaRepository<ClassificationSymbol, Long>, JpaSpecificationExecutor<ClassificationSymbol> {

    boolean existsByCode(String code);

    Optional<ClassificationSymbol> findByIdAndActiveFlagIsTrue(Long id);
}
