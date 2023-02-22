package com.example.apistackoverflow.controller;

import com.example.apistackoverflow.model.ApiUserDto;
import com.example.apistackoverflow.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/demo")
public class DemoController {
    private final UserService userService;

    public DemoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String runDemo() {
        List<ApiUserDto> list = userService.getAllUsers();
        log.info("API Response {}", list);
        return "Done!";
    }
}
