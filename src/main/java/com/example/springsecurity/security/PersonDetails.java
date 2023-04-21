package com.example.springsecurity.security;

import com.example.springsecurity.models.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {
    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return this.person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getLogin();
    }

    // Метод проверяет является ли аккаунт действительным
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Метод проверяет является ли аккаунт заблокированным
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Метод проверяет является ли пароль действительным
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Метод проверяет является ли аккаунт активным
    @Override
    public boolean isEnabled() {
        return true;
    }
}
