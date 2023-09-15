package com.upc.saveup.service;

import com.upc.saveup.dto.OrderDto;
import com.upc.saveup.model.Order;

import java.util.List;

public interface OrderService {
    public abstract OrderDto createOrder(OrderDto orderDto);
    public abstract void updateOrder(OrderDto orderDto);
    public abstract void deleteOrder(int id);
    public abstract Order getOrder(int id);
    public abstract List<Order> getAllOrders();
    public abstract boolean isOrderExist(int id);
}
