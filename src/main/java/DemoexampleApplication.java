package com.example.demoexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@Controller
public class DemoexampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoexampleApplication.class, args);
    }

    // Home page
    @GetMapping("/")
    public String home() {
        return "login";
    }

    // Login handler
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        if ("admin".equals(username) && "admin123".equals(password)) {
            model.addAttribute("message", "Welcome " + username + "!");
            return "success";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    // Test endpoint
    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "Hello World");
        return "success";
    }
}