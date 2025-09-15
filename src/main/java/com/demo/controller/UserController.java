package com.demo.controller;

import com.demo.dto.CreateUserRequest;
import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.security.Auth;
import com.demo.security.CurrentUser;
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

    // Only ADMIN can create users
    @PostMapping("/add")
    @Auth(role = Role.ADMIN)
    public ResponseEntity<User> add(@Valid @RequestBody CreateUserRequest req,
                                    @CurrentUser User adminUser) {
        // adminUser is available if you need auditing
        User saved = userService.addUser(req);
        return ResponseEntity.ok(saved);
    }

    // Normal USER token can get their user by id (or you can lock to self)
    @GetMapping("/get")
    @Auth // default Role.USER
    public ResponseEntity<User> get(@RequestParam("id") Long id,
                                    @CurrentUser User caller) {
        // OPTIONAL: restrict to self if you prefer
        // if (!caller.getId().equals(id) && caller.getRole()!=Role.ADMIN) { throw new Forbidden... }
        return ResponseEntity.ok(userService.getUser(id));
    }
}