package com.example.librarymanagement.constant;

public enum PunishmentForm {
    CARD_SUSPENSION("Tạm dừng thẻ"),
    CARD_REVOCATION("Thu hồi thẻ"),
    FINE("Phạt tiền");

    private final String description;

    PunishmentForm(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
