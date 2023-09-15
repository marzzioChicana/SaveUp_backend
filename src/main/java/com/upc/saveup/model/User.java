package com.upc.saveup.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email", length = 30, nullable = false)
    protected String email;
    @Column(name = "name", length = 20, nullable = false)
    protected String name;
    @Column(name = "address", length = 30, nullable = false)
    protected String address;
    @Column(name = "department", length = 20, nullable = false)
    protected String department;
    @Column(name = "district", length = 20, nullable = false)
    protected String district;
    @Column(name = "phone_number", length = 9, nullable = false)
    protected String phoneNumber;
    @Column(name = "password", length = 12, nullable = false)
    protected String password;
    @Column(name = "repeatPassword", length = 12, nullable = false)
    protected String repeatPassword;
}