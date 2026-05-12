package com.example.completelibrary.config;


import com.example.completelibrary.entity.UserLib;
import com.example.completelibrary.repository.UserLibRepo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserLibRepo userLibRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String authHeader = request.getHeader("Authorization");
            System.out.println("=== AUTH HEADER: " + authHeader);

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            System.out.println("=== USERNAME: " + username);

            if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null){
                System.out.println("=== SETTING AUTH");
                UserLib userLib = userLibRepo.findByName(username).orElseThrow(() -> new RuntimeException("Can not find"));

                if(jwtUtil.isTokenValid(token,userLib)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userLib,
                            null,
                            userLib.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            else{
                System.out.println("=== SKIPPING - auth: " + SecurityContextHolder.getContext().getAuthentication());
            }
            filterChain.doFilter(request,response);
        }
        catch (ExpiredJwtException e) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":401,\"message\":\"Token expired\"}");
            return;
        }
        catch (MalformedJwtException | SignatureException e) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":401,\"message\":\"Invalid token\"}");
            return;
        }
    }


}
