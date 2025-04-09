package com.example.librarymanagement.domain.dto.pagination;

import com.example.librarymanagement.constant.CommonConstant;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequestDto {

    @Parameter(description = "Page you want to retrieve (0..N)")
    private Integer pageNumb = CommonConstant.PAGE_NUM_DEFAULT;

    @Parameter(description = "Number of records per page")
    private Integer pageSize = CommonConstant.PAGE_SIZE_DEFAULT;

    public Integer getPageNumb() {
        if (pageNumb < 1)
            pageNumb = CommonConstant.PAGE_NUM_DEFAULT;
        return pageNumb - 1;
    }

    public Integer getPageSize() {
        if (pageSize < 1)
            pageSize = CommonConstant.PAGE_SIZE_DEFAULT;
        return pageSize;
    }
}
