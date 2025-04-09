package com.example.librarymanagement.domain.dto.request.reader;

import com.example.librarymanagement.constant.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReaderCardRequestDto {

    @NotNull(message = ErrorMessage.INVALID_ARRAY_IS_REQUIRED)
    @Size(min = 1, max = 100, message = ErrorMessage.INVALID_ARRAY_LENGTH)
    private Set<@NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED) Long>
            readerIds = new HashSet<>();

}
