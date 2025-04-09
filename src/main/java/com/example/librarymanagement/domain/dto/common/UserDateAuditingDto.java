package com.example.librarymanagement.domain.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UserDateAuditingDto extends DateAuditingDto{

    protected String createdBy;

    protected String lastModifiedBy;

}
