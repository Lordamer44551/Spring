package com.example.springsecurity.services;

import com.example.springsecurity.models.Cart;
import com.example.springsecurity.repositories.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getProductsByPersonId(int id) {
        return cartRepository.findByPersonId(id);
    }

    @Transactional
    public void deleteProductFromCartById(int id) {
        cartRepository.deleteProductFromCartById(id);
    }

    @Transactional
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
}
