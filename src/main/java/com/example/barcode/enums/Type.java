package com.example.barcode.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Type {
    LAPTOP("Ноутбук"),
    COMPUTER("Комп'ютер"),
    PHONE("Телефон");

    public final String name;

    public static List<String> getAllTypes() {
        return List.of(LAPTOP, COMPUTER, PHONE).stream().map(v -> v.getName()).collect(Collectors.toList());
    }
}
