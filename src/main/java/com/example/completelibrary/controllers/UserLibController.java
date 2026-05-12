package com.example.completelibrary.controllers;

import com.example.completelibrary.config.JwtUtil;
import com.example.completelibrary.dto.*;
import com.example.completelibrary.entity.UserLib;
import com.example.completelibrary.repository.UserLibRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserLibController {

    private final UserLibRepo userLibRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if(userLibRepo.findByName(registerRequest.getUserName()).isPresent()){
            return ResponseEntity.status(409).body("This user name already existed");
        }
        if(registerRequest.getPassword().length() < 6){
            return ResponseEntity.status(400).body("Pass word can not less than 6 characters");
        }
        UserLib userLib = new UserLib();
        userLib.setName(registerRequest.getUserName());

        userLib.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userLib.setRole("ROLE_USER");
        userLibRepo.save(userLib);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created");
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        UserLib userDetails = userLibRepo.findByName(request.getUserName()).orElseThrow(()-> new RuntimeException(""));
        String token = jwtUtil.generateToken(request.getUserName(),userDetails.getRole());
        String name = request.getUserName();
        String role = userDetails.getRole();
        Date expireIn = jwtUtil.getExpirationDateFromToken(token);

        return ResponseEntity.ok(new AuthResponse(name,role,token,expireIn,"Successful"));
    }
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getUserInfor(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserLib user = userLibRepo.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(new UserInfoResponse(user.getId(), user.getName(), user.getRole()));
    }
    @PostMapping("/register/admin")
    public ResponseEntity<String> adminRegister(@Valid @RequestBody AdminRegister registerRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getAdminUsername(),
                        registerRequest.getAdminPassword()
                )
        );
        UserLib userDetails = userLibRepo.findByName(registerRequest.getAdminUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!userDetails.getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only Admin can create admin account");
        }
        if(userLibRepo.findByName(registerRequest.getNewAdminUsername()).isPresent()) {
            return ResponseEntity.status(409).body("Username already existed");
        }
        if(registerRequest.getNewAdminPassword().length() < 6) {
            return ResponseEntity.status(400).body("Password cannot be less than 6 characters");
        }
        UserLib userLib = new UserLib();
        userLib.setName(registerRequest.getNewAdminUsername());
        userLib.setPassword(passwordEncoder.encode(registerRequest.getNewAdminPassword()));
        userLib.setRole("ROLE_ADMIN");
        userLibRepo.save(userLib);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created");
    }
    @PostMapping("/change/password")
    public ResponseEntity<AuthResponse> changePassword(@Valid @RequestBody ChangePassword request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserLib userLib = userLibRepo.findByName(request.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));
        userLib.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userLibRepo.save(userLib);
        String token = jwtUtil.generateToken(request.getUsername(),userLib.getRole());
        String name = request.getUsername();
        String role = userLib.getRole();
        Date expireIn = jwtUtil.getExpirationDateFromToken(token);
        return ResponseEntity.ok(new AuthResponse(name,role,token,expireIn,"Successful"));
    }

}