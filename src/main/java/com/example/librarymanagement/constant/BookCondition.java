package com.example.librarymanagement.constant;

public enum BookCondition {
    AVAILABLE("Có sẵn"),
    ON_LOAN("Đang mượn"),
    LOST("Báo mất");

    private final String name;

    BookCondition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
