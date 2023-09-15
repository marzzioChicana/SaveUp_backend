package com.upc.saveup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "card_name", length = 20, nullable = false)
    private String cardName;

    @Column(name = "card_number", length = 16, nullable = false)
    private String cardNumber;

    @Column(name = "cvv", length = 3, nullable = false)
    private String cvv;

    @Column(name = "type", length = 10, nullable = false)
    private String type;

    private int customerId;

    @ManyToMany
    @JoinTable(name = "customer_card",
            joinColumns = @JoinColumn(name = "card_id", foreignKey = @ForeignKey(name = "FK_card_customer_card")),
            inverseJoinColumns = @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "FK_customer_customer_card"))
    )
    @JsonIgnore
    private List<Customer> customers;
}
