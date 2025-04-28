package com.example.librarymanagement.security;

import com.example.librarymanagement.constant.RoleConstant;
import com.example.librarymanagement.domain.entity.User;
import com.example.librarymanagement.domain.entity.UserGroupRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AuthorityBuilder {
    public static List<GrantedAuthority> fromUser(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<UserGroupRole> roles = user.getUserGroup().getUserGroupRoles();
        for (UserGroupRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole().getCode().name()));
        }
        return authorities;
    }

    public static List<GrantedAuthority> fromReader() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleConstant.ROLE_READER.name()));
        return authorities;
    }
}
