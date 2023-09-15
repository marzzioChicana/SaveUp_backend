package com.upc.saveup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_card")
public class CustomerCard {
    @EmbeddedId
    private CustomerCardId id;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "FK_customer_card_customer"))
    private Customer customer;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id", foreignKey = @ForeignKey(name = "FK_card_customer_card"))
    @JsonIgnore
    private Card card;
}
