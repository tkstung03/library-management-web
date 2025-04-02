package com.example.librarymanagement.constant;

public enum BookCondition {
    AVAIABLE("Co san"),
    ON_LOAN("Dang muon"),
    LOST("Bi mat");

    private final String name;

    BookCondition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
