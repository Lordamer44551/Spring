package com.example.springsecurity.controllers;

import com.example.springsecurity.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public String getAllProduct(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "product/product";
    }

    @GetMapping("/info/{id}")
    public String getProductInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product/productInfo";
    }

    @PostMapping("/search")
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
}
