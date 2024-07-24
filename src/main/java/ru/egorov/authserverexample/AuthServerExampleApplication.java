package ru.egorov.authserverexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableR2dbcRepositories
public class AuthServerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerExampleApplication.class, args);
    }

}
