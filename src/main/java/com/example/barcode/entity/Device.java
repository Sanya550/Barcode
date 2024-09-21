package com.example.barcode.entity;

import lombok.Data;

@Data
public class Device {
    long id;
    String name;
    String type;
    String brand;
    String code;
}
