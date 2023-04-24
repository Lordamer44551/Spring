package com.example.springsecurity.services;

import com.example.springsecurity.models.Order;
import com.example.springsecurity.models.Person;
import com.example.springsecurity.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersByPerson(Person person) {
        return orderRepository.findByPerson(person);
    }

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
