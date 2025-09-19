package com.learn.codingtask.security;


import com.learn.codingtask.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip JWT validation for public endpoints
        if (path.equals("/api/employees/login")
        ||path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/webjars")
                || path.equals("/api/employees")
                || path.equals("/api/leaves/apply")
                || path.equals("/swagger-ui/index.html")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.equals("/api/employees/forgot-password")
                || path.equals("/api/employees/reset-password")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        String userName;

        try {
            userName = jwtService.validateTokenAndGetUserName(token);
            if (userName == null) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid or expired token");
                return;
            }
        } catch (Exception ex) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or malformed token: " + ex.getMessage());
            return;
        }

        //Set authentication in Spring Security context
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write("{\"status\":" + status + ",\"message\":\"" + message + "\"}");
    }
}
