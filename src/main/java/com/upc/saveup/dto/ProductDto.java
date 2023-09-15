package com.upc.saveup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private int id;
    private String name;
    private String description;
    private Double price;
    private int stock;
    private String expirationDate;
    private String image;
    private int companyId;
}
