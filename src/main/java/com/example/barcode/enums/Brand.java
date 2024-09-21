package com.example.barcode.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Brand {
    APPLE("Apple"),
    SAMSUNG("Samsung"),
    LENOVO("Lenovo"),
    ASUS("Asus");

    public final String name;

    public static List<String> getAllBrands() {
        return List.of(APPLE, SAMSUNG, ASUS, LENOVO).stream().map(v -> v.getName()).collect(Collectors.toList());
    }
}
