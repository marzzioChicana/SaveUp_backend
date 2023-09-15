package com.upc.saveup.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pay")
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "FK_pay_user"))
    private Customer customer;

    @Column(name = "amount", length = 20)
    private Double amount;

    @Column(name = "card_number", length = 20)
    private String cardNumber;

    @Column(name = "date", length = 20)
    private LocalDate date;

    @Column(name = "pay_address", length = 20)
    private String payAddress;

    @Column(name = "pay_department", length = 20)
    private String payDepartment;

    @Column(name = "pay_district", length = 20)
    private String payDistrict;
}
