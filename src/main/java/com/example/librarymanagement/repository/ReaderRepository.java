package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long>, JpaSpecificationExecutor<Reader> {

    boolean existsByCardNumber(String cardNumber);

    boolean existsByEmail(String email);

    Optional<Reader> findByCardNumber(String cardNumber);

    Optional<Reader> findByCardNumberAndEmail(String cardNumber, String email);

    List<Reader> findAllByIdIn(Set<Long> readerIds);
}
