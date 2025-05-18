package com.example.librarymanagement.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_definitions",
        uniqueConstraints = @UniqueConstraint(name = "UN_BOOK_DEFINITIONS_BOOK_NUMBER", columnNames = "book_number"))
public class BookDefinition { //biên mục
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_definition_id")
    @EqualsAndHashCode.Include
    private Long id; //mã biên mục

    @Column(name = "title", nullable = false)
    private String title; //nhan đề của biên mục

    @Column(name = "book_number")
    private String bookNumber; //Kí hiệu tên sách

    @Column(name = "publishing_year")
    private Integer publishingYear; //năm xuất bản

    @Column(name = "publication_place")
    private String publicationPlace; //Nơi xuất bản

    @Column(name = "price")
    private Double price;

    @Column(name = "edition")
    private String edition; //lần tái bản

    @Column(name = "reference_price")
    private Double referencePrice; //Giá tham khảo

    @Column(name = "page_count")
    private Integer pageCount; //số trang

    @Column(name = "book_size")
    private String bookSize; //kích thước

    @Column(name = "parallel_title")
    private String parallelTitle; // Nhan đề song song

    @Lob
    @Column(name = "summary")
    private String summary; //tóm lược

    @Column(name = "subtitle")
    private String subtitle; //phụ đề

    @Column(name = "additional_material")
    private String additionalMaterial; // Tài liệu đi kèm

    @Column(name = "keywords", length = 500)
    private String keywords; // Từ khóa tìm kiếm

    @Column(name = "isbn")
    private String isbn; // Mã đăng ký cá biệt/ mã định danh sách

    @Column(name = "language")
    private String language;

    @Column(name = "image_url")
    private String imageUrl;// Ảnh bìa

    @Column(name = "series")
    private String series;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "active_flag", nullable = false)
    private Boolean activeFlag = Boolean.TRUE;

    @OneToMany(mappedBy = "bookDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book> books = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_set_id", foreignKey = @ForeignKey(name = "FK_BOOK_DEFINITION_BOOK_SET_ID"), referencedColumnName = "book_set_id")
    @JsonIgnore
    private BookSet bookSet;//Bộ sách

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "FK_BOOK_DEFINITION_CATEGORY_ID"), referencedColumnName = "category_id", nullable = false)
    @JsonIgnore
    private Category category; // Danh mục

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", foreignKey = @ForeignKey(name = "FK_BOOK_DEFINITION_PUBLISHER_ID"), referencedColumnName = "publisher_id")
    @JsonIgnore
    private Publisher publisher;  // Nhà xuất bản

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_symbol_id", foreignKey = @ForeignKey(name = "FK_BOOK_DEFINITION_CLASSIFICATION_SYMBOL_ID"), referencedColumnName = "classification_symbol_id")
    @JsonIgnore
    private ClassificationSymbol classificationSymbol;// Kí hiệu phân loại

    @OneToMany(mappedBy = "bookDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookAuthor> bookAuthors = new ArrayList<>();  // Tác giả

    @OneToMany(mappedBy = "bookDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();
}
