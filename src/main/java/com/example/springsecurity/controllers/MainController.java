package com.example.springsecurity.controllers;

import com.example.springsecurity.enumm.Status;
import com.example.springsecurity.models.Cart;
import com.example.springsecurity.models.Order;
import com.example.springsecurity.models.Person;
import com.example.springsecurity.models.Product;
import com.example.springsecurity.security.PersonDetails;
import com.example.springsecurity.services.CartService;
import com.example.springsecurity.services.OrderService;
import com.example.springsecurity.services.PersonService;
import com.example.springsecurity.services.ProductService;
import com.example.springsecurity.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {
    private final PersonValidator personValidator;
    private final PersonService personService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;
    public MainController(PersonValidator personValidator, PersonService personService, ProductService productService, CartService cartService, OrderService orderService) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.productService = productService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/personalAccount")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();
        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        model.addAttribute("products", productService.getAllProduct());
        return "user/index";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person) {
        return "registration";
    }

    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        personService.register(person); 
        return "redirect:/personalAccount";
    }

    @GetMapping("/personalAccount/product/info/{id}")
    public String getProductInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "/user/productInfo";
    }

    @PostMapping("/personalAccount/product/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("start-price") String startPrice, @RequestParam("end-price") String endPrice, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "contract", required = false, defaultValue = "") String contract, Model model) {
        model.addAttribute("products", productService.getAllProduct());

        if(!startPrice.isEmpty() & !endPrice.isEmpty()) {
            if (!price.isEmpty()) {
                if (price.equals("sorted_by_ascending_price")) {
                    if (!contract.isEmpty()) {
                        if (contract.equals("furniture")) {
                            model.addAttribute("search_product", productService.getFilteredAndSortedProductsByTitle(search.toLowerCase(), Float.parseFloat(startPrice), Float.parseFloat(endPrice), 1, "asc"));
                        } else if (contract.equals("appliances")) {
                            model.addAttribute("search_product", productService.getFilteredAndSortedProductsByTitle(search.toLowerCase(), Float.parseFloat(startPrice), Float.parseFloat(endPrice), 3, "asc"));
                        } else if (contract.equals("clothes")) {
                            model.addAttribute("search_product", productService.getFilteredAndSortedProductsByTitle(search.toLowerCase(), Float.parseFloat(startPrice), Float.parseFloat(endPrice), 2, "asc"));
                        }
                    } else {
                        model.addAttribute("search_product", productService.getFilteredAndSortedProductsByTitle(search.toLowerCase(), Float.parseFloat(startPrice), Float.parseFloat(endPrice), "asc"));
                    }
                } else if (price.equals("sorted_by_descending_price")){
                    if (!contract.isEmpty()) {
                        if (contract.equals("furniture")) {
                            model.addAttribute("search_product", productService.getFilteredAndSortedProductsByTitle(search.toLowerCase(), Float.parseFloat(startPrice), Float.parseFloat(endPrice), 1, "desc"));
                        } else if (contract.equals("appliances")) {
                            model.addAttribute("search_product", productService.getFilteredAndSortedProductsByTitle(search.toLowerCase(), Float.parseFloat(startPrice), Float.parseFloat(endPrice), 3, "desc"));
                        } else if (contract.equals("clothes")) {
                            model.addAttribute("search_product", productService.getFilteredAndSortedProductsByTitle(search.toLowerCase(), Float.parseFloat(startPrice), Float.parseFloat(endPrice), 2, "desc"));
                        }
                    } else {
                        model.addAttribute("search_product", productService.getFilteredAndSortedProductsByTitle(search.toLowerCase(), Float.parseFloat(startPrice), Float.parseFloat(endPrice), "desc"));
                    }
                }
            } else {
                model.addAttribute("search_product", productService.getFilteredProductsByTitle(search.toLowerCase(), Float.parseFloat(startPrice), Float.parseFloat(endPrice)));
            }
        }

        model.addAttribute("value_search", search);
        model.addAttribute("value_startPrice", startPrice);
        model.addAttribute("value_endPrice", endPrice);
        return"product/product";
    }

    @GetMapping("cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id, Model model) {
        // Получение продукта по его id
        Product product = productService.getProductById(id);
        // Получение объекта аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Получение id пользователя из объекта
        int personId = personDetails.getPerson().getId();

        Cart cart = new Cart(personId, product.getId());
        cartService.saveCart(cart);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart (Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Получение id пользователя из объекта
        int personId = personDetails.getPerson().getId();

        List<Cart> cartList = cartService.getProductsByPersonId(personId);
        List<Product> productList = new ArrayList<>();

        // Получение товаров из корзины по их id
        for (Cart cart : cartList) {
            productList.add(productService.getProductById(cart.getProductId()));
        }

        // Вычисление цены заказа
        float totalPrice = 0;
        for (Product product : productList) {
            totalPrice += product.getPrice();
        }

        model.addAttribute("price", totalPrice);
        model.addAttribute("cart_product", productList);
        return "/user/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteProductFromCart(Model model, @PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Получение id пользователя из объекта
        int personId = personDetails.getPerson().getId();
        List<Cart> cartList = cartService.getProductsByPersonId(personId);
        List<Product> productList = new ArrayList<>();

        // Получение товаров из корзины по их id
        for (Cart cart : cartList) {
            productList.add(productService.getProductById(cart.getProductId()));
        }

        cartService.deleteProductFromCartById(id);
        return "redirect:/cart";
    }

    @GetMapping("order/create")
    public String order() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Получение id пользователя из объекта
        int personId = personDetails.getPerson().getId();

        List<Cart> cartList = cartService.getProductsByPersonId(personId);
        List<Product> productList = new ArrayList<>();

        // Получение товаров из корзины по их id
        for (Cart cart : cartList) {
            productList.add(productService.getProductById(cart.getProductId()));
        }

        // Вычисление цены заказа
        float totalPrice = 0;
        for (Product product : productList) {
            totalPrice += product.getPrice();
        }

        String uuid = UUID.randomUUID().toString();
        for(Product product : productList){
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.Оформлен);
            orderService.saveOrder(newOrder);
            cartService.deleteProductFromCartById(product.getId());
        }
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> orderList = orderService.getOrdersByPerson(personDetails.getPerson());
        model.addAttribute("orders", orderList);
        return "/user/orders";
    }
}
