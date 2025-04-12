package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.UserGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, Long> {
}
