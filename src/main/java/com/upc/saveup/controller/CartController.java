package com.upc.saveup.controller;

import com.upc.saveup.dto.CartDto;
import com.upc.saveup.repository.CartRepository;
import com.upc.saveup.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/api/saveup/v1")
public class CartController {
    @Autowired
    private CartService cartService;

    private final CartRepository cartRepository;

    public CartController(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    //EndPoint: localhost:8080/api/saveup/v1/carts
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/carts")
    public ResponseEntity<List<CartDto>> getAllCarts(){
        return new ResponseEntity<List<CartDto>>(cartService.getAllCarts(), HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/cart/order/{orderId}
    @GetMapping("/cart/order/{orderId}")
    public ResponseEntity<List<Map<String, Object>>> getCartByOrder(@PathVariable("orderId") int orderId) {
        List<Map<String, Object>> cartList = cartService.getCartByOrder(orderId);
        return ResponseEntity.ok(cartList);
    }

    //EndPoint: localhost:8080/api/saveup/v1/carts
    //Method: POST
    @Transactional
    @PostMapping("/carts")
    public ResponseEntity<CartDto> createCart(@RequestBody CartDto cartDto){
        return new ResponseEntity<>(cartService.createCart(cartDto), HttpStatus.CREATED);
    }

    // EndPoint: localhost:8080/api/saveup/v1/carts/{orderId}/{productId}
    // Method: DELETE
    @Transactional
    @DeleteMapping("/carts/{orderId}/{productId}")
    public ResponseEntity<String> deleteCart(@PathVariable("orderId") int orderId, @PathVariable("productId") int productId) {
        cartService.deleteCartByOrderAndProduct(orderId, productId);
        return ResponseEntity.ok("Cart deleted successfully");
    }

    // EndPoint: localhost:8080/api/saveup/v1/carts/order/{orderId}
    // Method: DELETE
    @Transactional
    @DeleteMapping("/carts/order/{orderId}")
    public ResponseEntity<String> deleteCartsByOrder(@PathVariable("orderId") int orderId) {
        cartService.deleteCartsByOrder(orderId);
        return ResponseEntity.ok("Carts deleted successfully");
    }

}
