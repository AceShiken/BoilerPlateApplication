package com.demo.controller;

import com.demo.dto.CreateUserRequest;
import com.demo.entity.User;
import com.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // POST /user/add
    @PostMapping("/add")
    public ResponseEntity<User> add(@Valid @RequestBody CreateUserRequest req) {
        User saved = userService.addUser(req);
        return ResponseEntity.ok(saved);
    }

    // GET /user/get?id=123
    @GetMapping("/get")
    public ResponseEntity<User> get(@RequestParam("id") Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}