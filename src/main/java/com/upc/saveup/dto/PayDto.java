package com.upc.saveup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayDto {
    private int id;
    private String customerName;
    private String customerLastName;
    private String phoneNumber;
    private String cardNumber;
    private String payAddress;
    private String payDepartment;
    private String payDistrict;
    private Double amount;
}
