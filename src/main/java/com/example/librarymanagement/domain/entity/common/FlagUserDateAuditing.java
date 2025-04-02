package com.example.librarymanagement.domain.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class FlagUserDateAuditing extends UserDateAuditing{

    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag = Boolean.FALSE;

    @Column(name = "active_flag", nullable = false)
    private Boolean activeFlag = Boolean.TRUE;
}
