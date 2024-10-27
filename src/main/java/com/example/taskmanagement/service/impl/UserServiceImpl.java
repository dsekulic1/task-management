package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.repository.PersonRepository;
import com.example.taskmanagement.repository.entity.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> optionalPerson = personRepository.findByUsername(username);
        if (optionalPerson.isEmpty()) {
            throw new UsernameNotFoundException("Username %s does not exist".formatted(username));
        }
        Person person = optionalPerson.get();

        return new User(person.getUsername(), person.getPassword(), getAuthorities(person));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Person person) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + person.getRole().name()));
    }
}
