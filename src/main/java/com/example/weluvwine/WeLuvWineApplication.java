package com.example.weluvwine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WeLuvWineApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeLuvWineApplication.class, args);
    }

}
