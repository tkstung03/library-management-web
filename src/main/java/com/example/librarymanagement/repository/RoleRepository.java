package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Byte> {
    Role findByCode(String code);
}
