package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.EventConstants;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.category.CategoryGroupRequestDto;
import com.example.librarymanagement.domain.dto.response.category.CategoryGroupTree;
import com.example.librarymanagement.domain.entity.CategoryGroup;
import com.example.librarymanagement.domain.mapper.CategoryGroupMapper;
import com.example.librarymanagement.domain.specification.CategoryGroupSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ConflictException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.CategoryGroupRepository;
import com.example.librarymanagement.service.CateforyGroupService;
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
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CategoryGroupServiceImpl implements CateforyGroupService {

    private static final String TAG = "Quản lý nhóm danh mục";

    private final CategoryGroupRepository categoryGroupRepository;

    private final CategoryGroupMapper categoryGroupMapper;

    private final MessageSource messageSource;

    private final LogService logService;

    @Override
    public void init(String categoryGroupCsvPath) {
        log.info("Initializing category group import from CSV: {}", categoryGroupCsvPath);

        if (categoryGroupRepository.count() > 0) {
            log.info("Category groups already exist in the database. Skipping import.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(categoryGroupCsvPath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 1) continue;

                CategoryGroup categoryGroup = new CategoryGroup();
                categoryGroup.setGroupName(values[0]);

                if (!categoryGroupRepository.existsByGroupName(categoryGroup.getGroupName())) {
                    categoryGroupRepository.save(categoryGroup);
                    log.info("Successfully saved category group: {}", categoryGroup.getGroupName());
                }
            }

            log.info("Category group import completed successfully.");
        } catch (IOException e) {
            log.error("Error while initializing category groups from CSV: {}", e.getMessage(), e);
        }
    }

    @Override
    public CommonResponseDto save(CategoryGroupRequestDto requestDto, String userId) {
        if (categoryGroupRepository.existsByGroupName(requestDto.getGroupName())) {
            throw new ConflictException(ErrorMessage.CategoryGroup.ERR_DUPLICATE_GROUP_NAME);
        }

        CategoryGroup categoryGroup = categoryGroupMapper.toCategoryGroup(requestDto);

        categoryGroup.setActiveFlag(true);
        categoryGroupRepository.save(categoryGroup);

        logService.createLog(TAG, EventConstants.ADD, "Thêm nhóm danh mục mới ID: " + categoryGroup.getId(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, categoryGroup);
    }

    @Override
    public CommonResponseDto update(Long id, CategoryGroupRequestDto requestDto, String userId) {
        if (categoryGroupRepository.existsByGroupName(requestDto.getGroupName())) {
            throw new ConflictException(ErrorMessage.CategoryGroup.ERR_DUPLICATE_GROUP_NAME);
        }

        CategoryGroup categoryGroup = findById(id);

        categoryGroup.setGroupName(requestDto.getGroupName());

        categoryGroupRepository.save(categoryGroup);

        logService.createLog(TAG, EventConstants.EDIT, "Cập nhật nhóm danh mục ID: " + categoryGroup.getId(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, categoryGroup);
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        CategoryGroup categoryGroup = findById(id);

        if (!categoryGroup.getCategories().isEmpty()) {
            throw new BadRequestException(ErrorMessage.CategoryGroup.ERR_HAS_LINKED_CATEGORIES);
        }

        categoryGroupRepository.delete(categoryGroup);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa nhóm danh mục ID: " + categoryGroup.getId(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, true);
    }

    @Override
    public PaginationResponseDto<CategoryGroup> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CATEGORY_GROUP);

        Page<CategoryGroup> page = categoryGroupRepository.findAll(
                CategoryGroupSpecification.filterCategoryGroups(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()),
                pageable);

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CATEGORY_GROUP, page);

        PaginationResponseDto<CategoryGroup> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CategoryGroup findById(Long id) {
        return categoryGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CategoryGroup.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id, String userId) {
        CategoryGroup categoryGroup = findById(id);

        categoryGroup.setActiveFlag(!categoryGroup.getActiveFlag());

        categoryGroupRepository.save(categoryGroup);

        logService.createLog(TAG, EventConstants.EDIT, "Thay đổi trạng thái nhóm danh mục ID: " + categoryGroup.getId() + ", trạng thái: " + categoryGroup.getActiveFlag(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, categoryGroup.getActiveFlag());
    }

    @Override
    public List<CategoryGroupTree> findTree() {
        List<CategoryGroupTree> groupTrees = categoryGroupRepository.findAll().stream()
                .map(CategoryGroupTree::new)
                .toList();

        int totalCount = groupTrees.stream()
                .mapToInt(CategoryGroupTree::getCount)
                .sum();

        CategoryGroupTree allCategoriesGroup = new CategoryGroupTree(-1, "Tất cả", totalCount, new ArrayList<>());

        List<CategoryGroupTree> responseDto = new ArrayList<>();
        responseDto.add(allCategoriesGroup);
        responseDto.addAll(groupTrees);

        return responseDto;
    }
}
