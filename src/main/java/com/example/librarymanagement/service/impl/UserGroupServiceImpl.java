package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.*;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.user.UserGroupRequestDto;
import com.example.librarymanagement.domain.dto.response.user.UserGroupResponseDto;
import com.example.librarymanagement.domain.entity.Role;
import com.example.librarymanagement.domain.entity.UserGroup;
import com.example.librarymanagement.domain.entity.UserGroupRole;
import com.example.librarymanagement.domain.mapper.UserGroupMapper;
import com.example.librarymanagement.domain.specification.UserGroupSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.ConflictException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.RoleRepository;
import com.example.librarymanagement.repository.UserGroupRepository;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.service.UserGroupService;
import com.example.librarymanagement.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private static final String TAG = "Quản lý nhóm người dùng";

    private final UserGroupRepository userGroupRepository;

    private final RoleRepository roleRepository;

    private final MessageSource messageSource;

    private final UserGroupMapper userGroupMapper;

    private final LogService logService;

    private UserGroup getEntity(Long id) {
        return userGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.UserGroup.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public void init() {
        log.info("Starting user group initialization");

        if (userGroupRepository.count() != 0) {
            log.info("User groups already initialized. Skipping initialization.");
            return;
        }

        UserGroup userGroup = new UserGroup();
        userGroup.setCode("ADMIN");
        userGroup.setName("Quản trị viên");
        Set<UserGroupRole> userGroupRoles = roleRepository.findAll().stream()
                .filter(role -> !role.getCode().equals(RoleConstant.ROLE_READER))
                .map(role ->  new UserGroupRole(role,userGroup))
                .collect(Collectors.toSet());

        userGroup.getUserGroupRoles().addAll(userGroupRoles);

        userGroupRepository.save(userGroup);

        log.info("User group initialization completed. Admin user group created with ID: {}", userGroup.getId());
    }

    @Override
    public CommonResponseDto save(UserGroupRequestDto requestDto, String userId) {
        if (userGroupRepository.existsByCode(requestDto.getCode())){
            throw new ConflictException(ErrorMessage.UserGroup.ERR_DUPLICATE_CODE, requestDto.getCode());
        }
        UserGroup userGroup = userGroupMapper.toUserGroup(requestDto);

        for (byte roleId : requestDto.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_ID, roleId));

            UserGroupRole userGroupRole = new UserGroupRole(role, userGroup);
            userGroup.getUserGroupRoles().add(userGroupRole);
        }

        userGroup.setActiveFlag(true);
        userGroupRepository.save(userGroup);

        logService.createLog(TAG, EventConstants.ADD, "Tạo nhóm người dùng mới: " + userGroup.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new UserGroupResponseDto(userGroup));
    }

    @Override
    public CommonResponseDto update(Long id, UserGroupRequestDto requestDto, String userId) {
        UserGroup userGroup = getEntity(id);

        if (!Objects.equals(userGroup.getCode(), requestDto.getCode()) &&  userGroupRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.UserGroup.ERR_DUPLICATE_CODE, requestDto.getCode());
        }

        Set<Byte> currentRoles = userGroup.getUserGroupRoles().stream()
                .map(userGroupRole -> userGroupRole.getRole().getId())
                .collect(Collectors.toSet());

        Set<Byte> newRoles = requestDto.getRoleIds();

        Set<Byte> rolesToAdd = new HashSet<>(newRoles);
        rolesToAdd.removeAll(currentRoles);

        Set<Byte> rolesToRemove = new HashSet<>(currentRoles);
        rolesToRemove.removeAll(newRoles);

        if (!rolesToAdd.isEmpty() || rolesToRemove.isEmpty()) {
            Map<Byte, Role> roleMap = roleRepository.findAllById(rolesToAdd).stream()
                    .collect(Collectors.toMap(Role::getId, Function.identity()));

            for (byte roleId : rolesToAdd) {
                Role role = roleMap.get(roleId);
                if (role != null) {
                    userGroup.getUserGroupRoles().add(new UserGroupRole(role, userGroup));
                }
            }

            // Remove old roles
            userGroup.getUserGroupRoles().removeIf(userGroupRole -> rolesToRemove.contains(userGroupRole.getRole().getId()));
        }

        userGroup.setCode(requestDto.getCode());
        userGroup.setName(requestDto.getName());
        userGroup.setNotes(requestDto.getNotes());
        userGroupRepository.save(userGroup);

        logService.createLog(TAG, EventConstants.EDIT, "Cập nhật nhóm người dùng ID: " + userGroup.getId() + ", tên mới: " + userGroup.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new UserGroupResponseDto(userGroup));
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        UserGroup userGroup = getEntity(id);

        // Nếu nhóm người dùng có người dùng liên kết, không cho phép xóa
        if (!userGroup.getUsers().isEmpty()) {
            throw new BadRequestException(ErrorMessage.UserGroup.ERR_HAS_LINKED_USERS);
        }

        userGroupRepository.delete(userGroup);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa nhóm người dùng: " + userGroup.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<UserGroupResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.USER_GROUP);

        Page<UserGroup> page = userGroupRepository.findAll(
                UserGroupSpecification.filterUserGroups(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()),
                pageable);

        List<UserGroupResponseDto> items = page.getContent().stream()
                .map(UserGroupResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.USER_GROUP, page);

        PaginationResponseDto<UserGroupResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public UserGroupResponseDto findById(Long id) {
        UserGroup userGroup = getEntity(id);
        return new UserGroupResponseDto(userGroup);
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id, String userId) {
        UserGroup userGroup = getEntity(id);

        userGroup.setActiveFlag(!userGroup.getActiveFlag());
        userGroupRepository.save(userGroup);

        logService.createLog(TAG, EventConstants.EDIT, "Thay đổi trạng thái nhóm người dùng: " + userGroup.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, userGroup.getActiveFlag());
    }
}
