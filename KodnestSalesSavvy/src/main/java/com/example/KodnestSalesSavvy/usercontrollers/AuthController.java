package com.example.KodnestSalesSavvy.usercontrollers;

import com.example.KodnestSalesSavvy.entities.LoginRequest;
import com.example.KodnestSalesSavvy.entities.User;
import com.example.KodnestSalesSavvy.userrepositories.JWTTokenRepository;
import com.example.KodnestSalesSavvy.userrepositories.UserRepository;
import com.example.KodnestSalesSavvy.userservices.AuthServiceContract;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5174",allowCredentials = "true")

public class AuthController {
    private final AuthServiceContract authService;
    private final JWTTokenRepository jwtTokenRepository;
    private final UserRepository userRepository;

    public AuthController(AuthServiceContract authService, JWTTokenRepository jwtTokenRepository, UserRepository userRepository) {
        this.authService = authService;
        this.jwtTokenRepository = jwtTokenRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            User user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            String token = authService.generateToken(user);
            Cookie cookie = new Cookie("authtoken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            cookie.setDomain("localhost");
            response.addCookie(cookie);
            response.addHeader("Set-Cookie",
                    String.format("authtoken=%s;HttpOnly;Path=/;Max-Age=3600;SameSite=None", token));

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Loggin succesfull");
            responseBody.put("role", user.getRole().name());
            responseBody.put("username", user.getUsername());
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/logout")
        public ResponseEntity<?> logout(HttpServletRequest request,HttpServletResponse response) {
        try {
            String token = null;
                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if ("authtoken".equals(cookie.getName())) {
                            token = cookie.getValue();
                            break;
                        }
                    }
                }
                if (token == null) {
                    return ResponseEntity.status(401).body("No token found");
                }
                if (!authService.validateToken(token)) {
                    return ResponseEntity.status(401).body("Invalid token");
                }
                String username = authService.extractUsername(token);
                Optional<User> userOptional = userRepository.findByUsername(username);

                if (userOptional.isPresent()) {
                    jwtTokenRepository.deleteByUserId(userOptional.get().getUserId());
                }

                Cookie cookie = new Cookie("authtoken", null);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                return ResponseEntity.ok("Logged out successfully");

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Logout failed");
            }
        }
    }


