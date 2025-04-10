package com.example.librarymanagement.domain.dto.response.category;

import com.example.librarymanagement.domain.dto.common.BaseEntityDto;
import com.example.librarymanagement.domain.entity.Category;
import com.example.librarymanagement.domain.entity.CategoryGroup;
import lombok.Getter;

@Getter
public class CategoryResponseDto {

    private final long id;

    private final String categoryName;

    private final String categoryCode;

    private final Boolean activeFlag;

    private final BaseEntityDto categoryGroup;

    public CategoryResponseDto(Category category){
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.categoryCode = category.getCategoryCode();
        this.activeFlag = category.getActiveFlag();

        CategoryGroup group =category.getCategoryGroup();
        this.categoryGroup = new BaseEntityDto(group.getId(), group.getGroupName());
    }
}
