package com.example.springsecurity.services;

import com.example.springsecurity.models.Person;
import com.example.springsecurity.repositories.PersonRepository;
import com.example.springsecurity.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Получение пользователя из таблицы по логину с формы аутентификации
        Optional<Person> person = personRepository.findByLogin(username);

        // Если пользователь не был найден
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new PersonDetails(person.get());
    }
}
