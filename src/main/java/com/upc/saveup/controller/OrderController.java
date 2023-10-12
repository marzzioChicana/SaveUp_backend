package com.upc.saveup.controller;

import com.upc.saveup.dto.OrderDto;
import com.upc.saveup.exception.ValidationException;
import com.upc.saveup.model.Order;
import com.upc.saveup.repository.OrderRepository;
import com.upc.saveup.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/api/saveup/v1")
public class OrderController {
    @Autowired
    private OrderService orderService;

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    //EndPoint: localhost:8080/api/saveup/v1/orders
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders(){
        return new ResponseEntity<List<Order>>(orderRepository.findAll(), HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/orders
    //Method: POST
    @Transactional
    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    //EndPoint: localhost:8080/api/saveup/v1/orders/{id}
    //Method: PUT
    @Transactional
    @PutMapping("/orders/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable("id") int id, @RequestBody OrderDto orderDto){
        boolean isExist=orderService.isOrderExist(id);
        if(isExist){
            orderDto.setId(id);
            orderService.updateOrder(orderDto);
            return new ResponseEntity<>("Order is updated succesfully", HttpStatus.OK);
        }else{
            throw new ValidationException("Error al actualizar el order");
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/orders/{id}
    //Method: DELETE
    @Transactional
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") int id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
