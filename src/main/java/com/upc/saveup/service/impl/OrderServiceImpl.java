package com.upc.saveup.service.impl;

import com.upc.saveup.dto.OrderDto;
import com.upc.saveup.exception.ResourceNotFoundException;
import com.upc.saveup.model.Order;
import com.upc.saveup.model.Pay;
import com.upc.saveup.repository.OrderRepository;
import com.upc.saveup.repository.PayRepository;
import com.upc.saveup.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private PayRepository payRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, PayRepository payRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.payRepository = payRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto){
        Order order = DtoToEntity(orderDto);
        order.setDate(LocalDate.now());

        return EntityToDto(orderRepository.save(order));
    }

    @Override
    public void updateOrder(OrderDto orderDto){
        Order order = DtoToEntity(orderDto);
        Pay pay = payRepository.findById(orderDto.getPayId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el pay con id: " + orderDto.getPayId()));
        order.setPay(pay);

        orderRepository.save(order);
    }

    @Override
    public Order getOrder(int id){ return orderRepository.findById(id).get(); }

    @Override
    public void deleteOrder(int id){ orderRepository.deleteById(id); }

    @Override
    public List<Order> getAllOrders(){ return (List<Order>) orderRepository.findAll(); }

    @Override
    public boolean isOrderExist(int id){ return orderRepository.existsById(id); }

    private OrderDto EntityToDto(Order order) { return modelMapper.map(order, OrderDto.class); }

    private Order DtoToEntity(OrderDto orderDto) { return modelMapper.map(orderDto, Order.class); }
}
