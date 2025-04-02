package com.example.librarymanagement.constant;

public enum CardStatus {
    ACTIVE("Đã kích hoạt"),
    INACTIVE("Chưa kích hoạt"),
    SUSPENDED("Tạm dừng"),
    REVOKED("Bị thu hồi");

    private final String name;

    CardStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
