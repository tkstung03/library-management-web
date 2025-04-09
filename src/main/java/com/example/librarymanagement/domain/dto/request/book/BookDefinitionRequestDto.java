package com.example.librarymanagement.domain.dto.request.book;

import com.example.librarymanagement.constant.ErrorMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDefinitionRequestDto {
    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String title;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String bookNumber;

    @NotNull(message = ErrorMessage.INVALID_NOT_NULL_FIELD)
    private Long categoryId;

    private Long publisherId;

    private Long bookSetId;

    private Long classificationSymbolId;

    private Integer pageCount;

    private Double price;

    private Double referencePrice;

    @NotNull(message = ErrorMessage.INVALID_ARRAY_IS_REQUIRED)
    private Set<@NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED) Long>
        authorIds = new HashSet<>(); //id tac gia

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String publicationPlace;

    private Integer publishingYear;

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String bookSize;

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String parallelTitle; //nhan de song song

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String subtitle;

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String edition;

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String additionalMaterial; //tai lieu di kem

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String summary;

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String isbn;

    @Max(value = 500, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String keywords;

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String language;

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String additionalInfo;

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String series;

    @Max(value = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String imageUrl;
}
