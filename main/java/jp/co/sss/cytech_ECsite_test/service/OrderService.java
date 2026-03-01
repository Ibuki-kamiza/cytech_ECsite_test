package jp.co.sss.cytech_ECsite_test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.cytech_ECsite_test.entity.Order;
import jp.co.sss.cytech_ECsite_test.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> findByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }
}