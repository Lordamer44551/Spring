package com.example.springsecurity.controllers;

import com.example.springsecurity.models.Person;
import com.example.springsecurity.security.PersonDetails;
import com.example.springsecurity.services.PersonService;
import com.example.springsecurity.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    private final PersonValidator personValidator;
    private final PersonService personService;

    public MainController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }


    @GetMapping("/index")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());
        System.out.println("ID пользователя: " + personDetails.getPerson().getId());
        System.out.println("Логин пользователя: " + personDetails.getPerson().getLogin());
        System.out.println("Пароль пользователя: " + personDetails.getPerson().getPassword());
        return "index";
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
        return "redirect:/index";
    }
}
