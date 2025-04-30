package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.EventConstants;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.category.CategoryRequestDto;
import com.example.librarymanagement.domain.dto.response.category.CategoryResponseDto;
import com.example.librarymanagement.domain.entity.Category;
import com.example.librarymanagement.domain.entity.CategoryGroup;
import com.example.librarymanagement.domain.mapper.CategoryMapper;
import com.example.librarymanagement.domain.specification.CategorySpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ConflictException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.CategoryGroupRepository;
import com.example.librarymanagement.repository.CategoryRepository;
import com.example.librarymanagement.service.CategoryService;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final String TAG = "Quản lý danh mục";

    private final LogService logService;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final MessageSource messageSource;

    private final CategoryGroupRepository categoryGroupRepository;

    @Override
    public void init(String categoriesCsvPath) {
        log.info("Initializing category import from CSV: {}", categoriesCsvPath);

        if (categoryRepository.count() > 0) {
            log.info("Categories already exist in the database. Skipping import.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(categoriesCsvPath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 3) continue;

                Category category = new Category();
                category.setCategoryName(values[0]);
                category.setCategoryCode(values[1]);

                Long categoryGroupId = Long.parseLong(values[2]);
                try {
                    CategoryGroup categoryGroup = categoryGroupRepository.findById(categoryGroupId)
                            .orElseThrow(() -> new RuntimeException("Category group not found with id: " + categoryGroupId));
                    category.setCategoryGroup(categoryGroup);
                } catch (RuntimeException e) {
                    log.warn("Skipping category '{}' due to missing category group with ID: {}", values[0], categoryGroupId);
                    continue;
                }

                if (!categoryRepository.existsByCategoryCode(category.getCategoryCode())) {
                    categoryRepository.save(category);
                    log.info("Successfully saved category: {} (Code: {})", category.getCategoryName(), category.getCategoryCode());
                }
            }

            log.info("Category import completed successfully.");
        } catch (IOException e) {
            log.error("Error while initializing categories from CSV: {}", e.getMessage(), e);
        }
    }

    @Override
    public CommonResponseDto save(CategoryRequestDto requestDto, String userId) {
        CategoryGroup categoryGroup = categoryGroupRepository.findByIdAndActiveFlagIsTrue(requestDto.getParentId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CategoryGroup.ERR_NOT_FOUND_ID, requestDto.getParentId()));

        if (categoryRepository.existsByCategoryName(requestDto.getCategoryName())) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_NAME);
        }

        if (categoryRepository.existsByCategoryCode(requestDto.getCategoryCode())) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_CODE);
        }

        Category category = categoryMapper.toCategory(requestDto);
        category.setCategoryGroup(categoryGroup);

        category.setActiveFlag(true);
        categoryRepository.save(category);

        logService.createLog(TAG, EventConstants.ADD, "Thêm danh mục mới Id: " + categoryGroup.getId(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new CategoryResponseDto(category));
    }

    @Override
    public CommonResponseDto update(Long id, CategoryRequestDto requestDto, String userId) {
        Category category = findById(id);

        if (!Objects.equals(category.getCategoryName(), requestDto.getCategoryName())
                && categoryRepository.existsByCategoryName(requestDto.getCategoryName())) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_NAME);
        }

        if (!Objects.equals(category.getCategoryCode(), requestDto.getCategoryCode())
                && categoryRepository.existsByCategoryCode(requestDto.getCategoryCode())) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_CODE);
        }

        if (!Objects.equals(category.getCategoryGroup().getId(), requestDto.getParentId())) {
            CategoryGroup categoryGroup = categoryGroupRepository.findByIdAndActiveFlagIsTrue(requestDto.getParentId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.CategoryGroup.ERR_NOT_FOUND_ID, requestDto.getParentId()));
            category.setCategoryGroup(categoryGroup);
        }

        category.setCategoryName(requestDto.getCategoryName());
        category.setCategoryCode(requestDto.getCategoryCode());

        categoryRepository.save(category);

        logService.createLog(TAG, EventConstants.EDIT, "Cập nhật danh mục Id: " + category.getId(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new CategoryResponseDto(category));
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        Category category = findById(id);

        if (!category.getBookDefinitions().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Category.ERR_HAS_LINKED_BOOKS);
        }

        categoryRepository.delete(category);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa danh mục Id: " + category.getId(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, true);
    }

    @Override
    public PaginationResponseDto<CategoryResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CATEGORY);

        Page<Category> page = categoryRepository.findAll(
                CategorySpecification.filterCategories(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()),
                pageable);

        List<CategoryResponseDto> items = page.getContent().stream()
                .map(CategoryResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CATEGORY, page);

        PaginationResponseDto<CategoryResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id, String userId) {
        Category category = findById(id);

        category.setActiveFlag(!category.getActiveFlag());

        categoryRepository.save(category);

        logService.createLog(TAG, EventConstants.EDIT, "Thay đổi trạng thái danh mục Id: " + category.getId() + ", trạng thái: " + category.getActiveFlag(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, category.getActiveFlag());
    }
}
