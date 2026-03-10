package com.example.KodnestSalesSavvy.filters;

import com.example.KodnestSalesSavvy.entities.Role;
import com.example.KodnestSalesSavvy.entities.User;
import com.example.KodnestSalesSavvy.userrepositories.UserRepository;
import com.example.KodnestSalesSavvy.userservices.AuthServiceContract;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebFilter(urlPatterns = {"/api/*","/admin/*"})
@Component
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final AuthServiceContract authService;
    private final UserRepository userRepository;
    private static final String ALLOWED_ORIGIN = "http://localhost:5174";
    private static final String[] PUBLIC_PATHS = {
            "/api/users/register",
            "/api/auth/login",
            "/api/auth/logout"
    };

    public AuthenticationFilter(AuthServiceContract authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        setCORSHeaders(httpResponse);

        String requestURI = httpRequest.getRequestURI();
        logger.info("Request URI: {}", requestURI);

        if (Arrays.asList(PUBLIC_PATHS).contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            setCORSHeaders(httpResponse);
            return;
        }
        String token = getAuthTokenFromCookies(httpRequest);
        if (token == null || !authService.validateToken(token)) {
            sendErrorResponse(httpResponse,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized: Invalid or missing token");
            return;
        }
        String username = authService.extractUsername(token);
        Optional<User> userOptional=userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            sendErrorResponse(httpResponse,HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: User not found");
            return;
        }

        User authenticatedUser = userOptional.get();
        Role role = authenticatedUser.getRole();

        logger.info("Authenticated User: {}, Role: {}",
                authenticatedUser.getUsername(), role);
        if (requestURI.startsWith("/admin/")&& !Role.ADMIN.equals(role)) {
            sendErrorResponse(httpResponse,HttpServletResponse.SC_FORBIDDEN, "Forbidden: Admin access required");
            return;
        }

        if (requestURI.startsWith("/api/orders") && role != Role.CUSTOMER) {
            sendErrorResponse(httpResponse,HttpServletResponse.SC_FORBIDDEN, "Forbidden: Customer access required");
            return;
        }

        httpRequest.setAttribute("authenticatedUser", authenticatedUser);
        chain.doFilter(request, response);
    }
    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message)
            throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write(message);
    }
    private String getAuthTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie ->
                            "authtoken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}