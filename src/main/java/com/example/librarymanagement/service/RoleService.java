package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.entity.Role;

import java.util.List;

public interface RoleService {
    void init();

    List<Role> getRoles();
}
