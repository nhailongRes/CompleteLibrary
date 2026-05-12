package com.example.completelibrary.controllers;
import com.example.completelibrary.dto.AdminRequest;
import com.example.completelibrary.dto.AuthResponse;
import com.example.completelibrary.dto.RecommendRequest;
import com.example.completelibrary.dto.UserInfoResponse;
import com.example.completelibrary.repository.UserLibRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserLibRepo userLibRepo;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/users")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers(@Valid @RequestBody AdminRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        List<UserInfoResponse> users = userLibRepo.findAll()
                .stream()
                .map(u -> new UserInfoResponse(u.getId(), u.getName(), u.getRole()))
                .toList();
        return ResponseEntity.ok(users);
    }
}
