package com.upc.saveup.service.impl;

import com.upc.saveup.dto.CartDto;
import com.upc.saveup.exception.ResourceNotFoundException;
import com.upc.saveup.model.Cart;
import com.upc.saveup.model.Order;
import com.upc.saveup.model.Product;
import com.upc.saveup.repository.CartRepository;
import com.upc.saveup.repository.OrderRepository;
import com.upc.saveup.repository.ProductRepository;
import com.upc.saveup.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    private final JdbcTemplate jdbcTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, OrderRepository orderRepository, ModelMapper modelMapper, JdbcTemplate jdbcTemplate) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CartDto createCart(CartDto cartDto) {
        Cart cart = DtoToEntity(cartDto);
        Product product = productRepository.findById(cartDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id: " + cartDto.getProductId()));
        cart.setProduct(product);

        Order order = orderRepository.findById(cartDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la orden con id: " + cartDto.getOrderId()));
        cart.setOrder(order);

        return EntityToDto(cartRepository.save(cart));
    }

    @Override
    public void updateCart(Cart cart) { cartRepository.save(cart); }

    @Override
    public Cart getCart(int id) { return cartRepository.findById(id).get(); }

    @Override
    public void deleteCart(int id) { cartRepository.deleteById(id); }

    @Override
    public void deleteCartsByOrder(int orderId) { cartRepository.deleteByOrderId(orderId); }

    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(this::EntityToDto).toList();
    }

    @Override
    public boolean isCartExist(int id) { return cartRepository.existsById(id); }

    @Override
    public List<Map<String, Object>> getCartByOrder(int orderId) {
        String query = "SELECT p.id, p.description, p.expiration_date, p.image, p.name, p.price, p.stock, p.company_id " +
                "FROM saveup.cart c " +
                "INNER JOIN saveup.product p ON c.product_id = p.id " +
                "WHERE c.order_id = ?";

        return jdbcTemplate.queryForList(query, orderId);
    }

    @Override
    public void deleteCartByOrderAndProduct(int orderId, int productId) {
        cartRepository.deleteByOrderIdAndProductId(orderId, productId);
    }

    private CartDto EntityToDto(Cart cart) { return modelMapper.map(cart, CartDto.class); }

    private Cart DtoToEntity(CartDto cartDto) {
        return modelMapper.map(cartDto, Cart.class);
    }
}
