package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.Gender;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.author.AuthorRequestDto;
import com.example.librarymanagement.domain.entity.Author;
import com.example.librarymanagement.domain.mapper.AuthorMapper;
import com.example.librarymanagement.repository.AuthorRepository;
import com.example.librarymanagement.service.AuthorService;
import com.example.librarymanagement.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private static final String TAG = "Quản lý tác giả";

    private final LogService logService;

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final MessageSource messageSource;

    @Override
    public void init(String authorsCsvPath) {
        log.info("Initializing author import from CSV: {}", authorsCsvPath);

        if (authorRepository.count() > 0) {
            log.info("Authors already exist in the database. Skipping import.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(authorsCsvPath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 10) continue;

                Author author = new Author();
                author.setFullName(values[0]);
                author.setCode(values[1]);
                author.setPenName(values[2].isEmpty() ? null : values[2]);
                author.setGender(Gender.valueOf(values[3]));
                author.setDateOfBirth(LocalDate.parse(values[4]));
                if (!values[5].isEmpty()) {
                    author.setDateOfDeath(LocalDate.parse(values[5]));
                }
                author.setTitle(values[6]);
                author.setResidence(values[7]);
                author.setAddress(values[8].isEmpty() ? null : values[8]);
                author.setNotes(values[9].isEmpty() ? null : values[9]);

                if (!authorRepository.existByCode(author.getCode())) {
                    authorRepository.save(author);
                    log.info("Successfully saved author: {} (Code: {})", author.getFullName(), author.getCode());
                }
            }

            log.info("Author import completed successfully.");

        } catch (IOException e) {
            log.error("error while saving author: {}", e.getMessage(), e);
        }
    }

    @Override
    public CommonResponseDto save(AuthorRequestDto requestDto, String userId) {
        return null;
    }

    @Override
    public CommonResponseDto update(AuthorRequestDto requestDto, Long id, String userId) {
        return null;
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        return null;
    }

    @Override
    public PaginationResponseDto<Author> findAll(PaginationFullRequestDto requestDto) {
        return null;
    }

    @Override
    public Author findById(Long id) {
        return null;
    }

    @Override
    public CommonResponseDto toggleActivityStatus(Long id, String userId) {
        return null;
    }
}
