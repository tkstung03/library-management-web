package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.dto.response.statistics.CategoryStatisticsResponseDto;
import com.example.librarymanagement.domain.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    boolean existsByCategoryName(String categoryName);

    boolean existsByCategoryCode(String categoryCode);

    Optional<Category> findByIdAndActiveFlagIsTrue(Long id);

    //Thống kê sách theo Category
    @Query("SELECT new com.example.librarymanagement.domain.dto.response.statistics.CategoryStatisticsResponseDto(c.categoryName, COUNT(b.id)) " +
            "FROM Category c " +
            "LEFT JOIN c.bookDefinitions bd " +
            "LEFT JOIN bd.books b " +
            "GROUP BY c.id, c.categoryName " +
            "ORDER BY COUNT(b.id) DESC")
    List<CategoryStatisticsResponseDto> findCategoryStatistics(Pageable pageable);
}
