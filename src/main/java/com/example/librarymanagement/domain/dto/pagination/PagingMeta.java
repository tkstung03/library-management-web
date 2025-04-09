package com.example.librarymanagement.domain.dto.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagingMeta {

    private Long totalElements;

    private Integer totalPages;

    private Integer pageNumb;

    private Integer pageSize;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sortBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sortType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String keyword;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String searchBy;

}
