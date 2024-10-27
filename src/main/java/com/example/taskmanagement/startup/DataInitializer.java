package com.example.taskmanagement.startup;

import com.example.taskmanagement.repository.PersonRepository;
import com.example.taskmanagement.repository.entity.Person;
import com.example.taskmanagement.repository.entity.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;



    @Override
    public void run(String... args) {
        if (personRepository.findByUsername(adminUsername).isEmpty()) {
            Person adminUser = new Person();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole(RoleEnum.ADMIN);
            adminUser.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
            personRepository.save(adminUser);
        }
    }
}
