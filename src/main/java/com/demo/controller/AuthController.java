package com.demo.controller;

import com.demo.service.AuthService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> token(@RequestBody IssueTokenRequest req) {
        String jwt = authService.issueToken(req.getUserId(), req.getPhone());
        return ResponseEntity.ok(Map.of("token", jwt, "token_type", "Bearer", "expires_in", "3600"));
    }

    @Data
    public static class IssueTokenRequest {
        @NotNull private Long userId;
        @NotBlank private String phone;
    }
}
