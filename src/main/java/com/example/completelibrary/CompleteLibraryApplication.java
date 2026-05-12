package com.example.completelibrary;

import com.example.completelibrary.entity.UserLib;
import com.example.completelibrary.repository.UserLibRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CompleteLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompleteLibraryApplication.class, args);
    }
    @Bean
    CommandLineRunner initAdmin(UserLibRepo userLibRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userLibRepo.findByName("admin").isEmpty()) {
                UserLib admin = new UserLib();
                admin.setName("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ROLE_ADMIN");
                userLibRepo.save(admin);
                System.out.println("Admin created: admin/admin123");
            }
        };
    }

}
