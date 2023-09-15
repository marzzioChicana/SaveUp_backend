package com.upc.saveup.repository;

import com.upc.saveup.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    void deleteByOrderIdAndProductId(int orderId, int productId);

    void deleteByOrderId(int orderId);
}
