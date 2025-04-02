package com.example.librarymanagement.constant;

public enum BookStatus {
    USABLE("Sử dụng được"),
    DAMAGED("Rách nát"),
    OUTDATED("Lỗi thời"),
    INFESTED("Mối mọt"),
    OBSOLETE_PROGRAM("Chương trình cũ");

    private final String name;

    BookStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
