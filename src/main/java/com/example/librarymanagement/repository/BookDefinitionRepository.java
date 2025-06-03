package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.dto.response.bookdefinition.BookDefinitionResponseDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookForReaderResponseDto;
import com.example.librarymanagement.domain.entity.BookDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookDefinitionRepository extends JpaRepository<BookDefinition, Long>, JpaSpecificationExecutor<BookDefinition> {
    //Kiểm tra tồn tại
    boolean existsByBookNumber(String bookNumber);

    Optional<BookDefinition> findByIdAndActiveFlagIsTrue(Long Id);

    @Query("SELECT DISTINCT bd.id FROM BookDefinition bd JOIN Book b ON b.bookDefinition = bd")
    Set<Long> findAllBookDefinitionIdsWithBook();

    @Query("""
            SELECT new com.example.librarymanagement.domain.dto.response.bookdefinition.BookDefinitionResponseDto(b) 
                        FROM BookDefinition b 
                        WHERE b.id IN :ids
            """)
    List<BookDefinitionResponseDto> findBookDefinitionsByIds(@Param("ids") Set<Long> ids);

    @Query("SELECT new com.example.librarymanagement.domain.dto.response.bookdefinition.BookForReaderResponseDto(bd) FROM BookDefinition bd WHERE bd.id IN :ids")
    List<BookForReaderResponseDto> findByIdIn(@Param("ids") List<Long> ids);

    @Query("""
                SELECT bd
                FROM BookDefinition bd
                LEFT JOIN bd.books b
                LEFT JOIN b.bookBorrows bb
                GROUP BY bd
                ORDER BY COUNT(bb.id) DESC
            """)
    Page<BookDefinition> findBooksOrderByBorrowCount(Pageable pageable);
}
