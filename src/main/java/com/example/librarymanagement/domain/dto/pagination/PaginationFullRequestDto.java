package com.example.librarymanagement.domain.dto.pagination;

import com.example.librarymanagement.constant.CommonConstant;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationFullRequestDto extends PaginationSortRequestDto {

    @Parameter(description = "Keyword to search")
    private String keyword = CommonConstant.EMPTY_STRING;

    @Parameter(description = "Search by")
    private String searchBy = CommonConstant.EMPTY_STRING;

    @Getter
    @Parameter(description = "Active")
    private Boolean activeFlag;

    public String getKeyword() {
        return keyword.trim();
    }

    public String getSearchBy() {
        return searchBy.trim();
    }
}
