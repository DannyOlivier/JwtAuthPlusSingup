package com.example.demo;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        this.users.save(User.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER"))
                .enabled(true)
                .build()
        );

        this.users.save(User.builder()
                .username("admin")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .enabled(true)
                .build()
        );

        log.debug("printing all users...");
        this.users.findAll().forEach(v -> log.debug(" User :" + v.toString()));
    }
}
