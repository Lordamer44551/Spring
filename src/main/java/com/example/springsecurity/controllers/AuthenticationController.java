package com.example.springsecurity.controllers;

import com.example.springsecurity.models.Product;
import com.example.springsecurity.repositories.ProductCategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/authentication")
    public String login() {
        return "authentication";
    }

    @PostMapping("/logout")
    public String logout() {
        return "authentication";
    }


}
