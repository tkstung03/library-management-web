package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.repository.BookAuthorRepository;
import com.example.librarymanagement.service.BookAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookAuthorServiceImpl implements BookAuthorService {

    private BookAuthorRepository bookAuthorRepository;

}
